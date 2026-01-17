package com.example.visiograph.viewmodel.kerusakan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DetailKerusakan
import com.example.visiograph.modeldata.toDetailKerusakan
import com.example.visiograph.repositori.RepositoryDataKerusakan
import com.example.visiograph.uicontroller.route.kerusakan.DestinasiKerusakanDetail
import kotlinx.coroutines.launch

class DetailKerusakanVM(
    savedStateHandle: SavedStateHandle,
    private val repoKerusakan: RepositoryDataKerusakan
) : ViewModel() {

    // Ambil ID dari argument navigasi
    private val idKerusakan: Int = checkNotNull(savedStateHandle[DestinasiKerusakanDetail.itemIdArg] ?: 0)

    var detailUiState by mutableStateOf(DetailKerusakan())
        private set

    init {
        getKerusakanById()
    }

    fun getKerusakanById() {
        viewModelScope.launch {
            try {
                val kerusakan = repoKerusakan.getKerusakanById(idKerusakan)
                detailUiState = kerusakan.toDetailKerusakan()
            } catch (e: Exception) {
                // Handle error load data
            }
        }
    }

    suspend fun deleteKerusakan() {
        try {
            repoKerusakan.deleteKerusakan(idKerusakan)
        } catch (e: Exception) {
            // Handle error hapus data
        }
    }
}