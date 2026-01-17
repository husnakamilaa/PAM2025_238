package com.example.visiograph.viewmodel.peminjaman

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataPeminjaman
import com.example.visiograph.repositori.RepositoryDataPeminjaman
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

sealed class ListPeminjamanUiState {
    data class Success(val peminjaman: List<DataPeminjaman>) : ListPeminjamanUiState()
    object Error : ListPeminjamanUiState()
    object Loading : ListPeminjamanUiState()
}

class ListPeminjamanVM(private val repo: RepositoryDataPeminjaman) : ViewModel() {

    var peminjamanUiState: ListPeminjamanUiState by mutableStateOf(ListPeminjamanUiState.Loading)
        private set

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedStatus = MutableStateFlow("Semua")
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _dataPusat = MutableStateFlow<List<DataPeminjaman>>(emptyList())

    @OptIn(kotlinx.coroutines.FlowPreview::class)
    val filteredPeminjaman = combine(
        searchText.debounce(300L),
        selectedStatus,
        _dataPusat
    ) { text, status, list ->
        list.filter {
            (it.nama_barang?.contains(text, ignoreCase = true) == true ||
                    it.nama_anggota?.contains(text, ignoreCase = true) == true) &&
                    (status == "Semua" || it.status_pinjam == status)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init { getPeminjaman() }


    fun getPeminjaman() {
        viewModelScope.launch {
            println("DEBUG_PINJEM: getBarang() CALLED")
            peminjamanUiState = ListPeminjamanUiState.Loading
            try {
                val list = repo.getAllPeminjaman()
                _dataPusat.value = list
                println("DEBUG_PINJEM: data size = ${list.size}")
                peminjamanUiState = ListPeminjamanUiState.Success(list)
            } catch (e: Exception) {
                println("DEBUG_PINJEM: ERROR = ${e.message}")
                peminjamanUiState = ListPeminjamanUiState.Error
            }
        }
    }
    fun onSearchChange(query: String) {
        _searchText.value = query }
    fun onStatusChange(status: String) {
        _selectedStatus.value = status }
}