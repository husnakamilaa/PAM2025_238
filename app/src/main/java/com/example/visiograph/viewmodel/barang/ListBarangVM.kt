package com.example.visiograph.viewmodel.barang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataBarang
import com.example.visiograph.modeldata.doesMatchSearchQuery
import com.example.visiograph.repositori.RepositoryDataBarang
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ListBarangUiState {
    data class Success(val barang: List<DataBarang>) : ListBarangUiState()
    object Error : ListBarangUiState()
    object Loading : ListBarangUiState()
}

class ListBarangVM(private val repo: RepositoryDataBarang) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedKategori = MutableStateFlow("All")
    val selectedKategori = _selectedKategori.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _dataPusat = MutableStateFlow<List<DataBarang>>(emptyList())

    @OptIn(FlowPreview::class)
    val filteredBarang = combine(
        searchText.debounce(500L),
        selectedKategori,
        _dataPusat
    ) { text, kategori, list ->
        _isSearching.update { true }

        val result = if (text.isBlank() && kategori == "All") {
            list
        } else {
            list.filter {
                it.doesMatchSearchQuery(text) &&
                        (kategori == "All" || it.kategori == kategori)
            }
        }

        delay(300L)
        _isSearching.update { false }
        result
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    var barangUiState: ListBarangUiState by mutableStateOf(ListBarangUiState.Loading)
        private set

    init { getBarang() }

    fun getBarang() {
        viewModelScope.launch {
            println("DEBUG_VM: getBarang() CALLED")
            barangUiState = ListBarangUiState.Loading
            try {
                val list = repo.getAllBarang()
                _dataPusat.value = list
                println("DEBUG_VM: data size = ${list.size}")
                barangUiState = ListBarangUiState.Success(list)
            } catch (e: Exception) {
                println("DEBUG_VM: ERROR = ${e.message}")
                barangUiState = ListBarangUiState.Error
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onKategoriChange(kategori: String) {
        _selectedKategori.value = kategori
    }
}