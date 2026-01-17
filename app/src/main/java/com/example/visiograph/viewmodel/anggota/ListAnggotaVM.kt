package com.example.visiograph.viewmodel.anggota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataAnggota
import com.example.visiograph.modeldata.doesMatchSearchQuery
import com.example.visiograph.repositori.RepositoryDataAnggota
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

sealed class ListAnggotaUiState {
    data class Success(val anggota: List<DataAnggota>) : ListAnggotaUiState()
    object Error : ListAnggotaUiState()
    object Loading : ListAnggotaUiState()
}

class ListAnggotaVM(private val repo: RepositoryDataAnggota) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedDivisi = MutableStateFlow("All")
    val selectedDivisi = _selectedDivisi.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _dataPusat = MutableStateFlow<List<DataAnggota>>(emptyList())

    @OptIn(FlowPreview::class)
    val filteredAnggota = combine(
        searchText.debounce(500L),
        selectedDivisi,
        _dataPusat
    ) { text, divisi, list ->
        _isSearching.update { true }
        val result = if (text.isBlank() && divisi == "All") {
            list
        } else {
            list.filter {
                it.doesMatchSearchQuery(text) && (divisi == "All" || it.divisi == divisi)
            }
        }
        delay(300L) // Simulasi loading kecil
        _isSearching.update { false }
        result
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    var anggotaUiState: ListAnggotaUiState by mutableStateOf(ListAnggotaUiState.Loading)
        private set

    init { getAnggota() }

    fun getAnggota() {
        viewModelScope.launch {
            anggotaUiState = ListAnggotaUiState.Loading
            try {
                val list = repo.getAllAnggota()
                _dataPusat.value = list
                anggotaUiState = ListAnggotaUiState.Success(list)
            } catch (e: Exception) {
                anggotaUiState = ListAnggotaUiState.Error
            }
        }
    }

    fun onSearchTextChange(text: String) { _searchText.value = text }
    fun onDivisiChange(divisi: String) { _selectedDivisi.value = divisi }
}