package com.example.visiograph.viewmodel.anggota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DetailAnggota
import com.example.visiograph.modeldata.toDetailAnggota
import com.example.visiograph.repositori.RepositoryDataAnggota
import com.example.visiograph.uicontroller.route.anggota.DestinasiAnggotaDetail
import kotlinx.coroutines.launch

class DetailAnggotaVM(
    savedStateHandle: SavedStateHandle,
    private val repo: RepositoryDataAnggota
) : ViewModel() {
    private val anggotaId: Int = checkNotNull(savedStateHandle[DestinasiAnggotaDetail.itemIdArg]?: 0)

    var detailUiState by mutableStateOf(DetailAnggota())
        private set

    init {
        viewModelScope.launch {
            try {
                val data = repo.getAnggotaById(anggotaId)
                detailUiState = data.toDetailAnggota()
            } catch (e: Exception) {
                // Log errornya di Logcat dengan tag "DEBUG_VM"
                println("ERROR_VM: ${e.message}")
            }
        }
    }
    suspend fun deleteAnggota() {
        repo.deleteAnggota(anggotaId)
    }
}