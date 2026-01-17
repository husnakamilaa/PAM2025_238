package com.example.visiograph.viewmodel.kerusakan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataKerusakan
import com.example.visiograph.repositori.RepositoryDataKerusakan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ListKerusakanUiState {
    data class Success(val kerusakan: List<DataKerusakan>) : ListKerusakanUiState()
    object Error : ListKerusakanUiState()
    object Loading : ListKerusakanUiState()
}

class ListKerusakanVM(private val repo: RepositoryDataKerusakan) : ViewModel() {

    var kerusakanUiState: ListKerusakanUiState by mutableStateOf(ListKerusakanUiState.Loading)
        private set

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // Untuk Menu Anchor / Filter status perbaikan
    private val _selectedStatus = MutableStateFlow("Semua")
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _dataPusat = MutableStateFlow<List<DataKerusakan>>(emptyList())

    @OptIn(kotlinx.coroutines.FlowPreview::class)
    val filteredKerusakan = combine(
        searchText.debounce(300L),
        selectedStatus,
        _dataPusat
    ) { text, status, list ->
        list.filter {
            (it.nama_barang?.contains(text, ignoreCase = true) == true) &&
                    (status == "Semua" || it.status_perbaikan == status)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        getKerusakan()
    }

    fun getKerusakan() {
        viewModelScope.launch {
            kerusakanUiState = ListKerusakanUiState.Loading
            try {
                val list = repo.getAllKerusakan()
                _dataPusat.value = list
                kerusakanUiState = ListKerusakanUiState.Success(list)
            } catch (e: Exception) {
                kerusakanUiState = ListKerusakanUiState.Error
            }
        }
    }

    fun onSearchChange(query: String) {
        _searchText.value = query
    }

    fun onStatusChange(status: String) {
        _selectedStatus.value = status
    }
}