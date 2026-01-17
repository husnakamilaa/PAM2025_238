package com.example.visiograph.viewmodel.anggota

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DetailAnggota
import com.example.visiograph.modeldata.UIStateAnggota
import com.example.visiograph.modeldata.toDataAnggota
import com.example.visiograph.modeldata.toDetailAnggota
import com.example.visiograph.modeldata.toUiStateAnggota
import com.example.visiograph.repositori.RepositoryDataAnggota
import com.example.visiograph.uicontroller.route.anggota.DestinasiAnggotaDetail
import com.example.visiograph.uicontroller.route.anggota.DestinasiAnggotaEdit
import kotlinx.coroutines.launch

class EditAnggotaVM(
    savedStateHandle: SavedStateHandle,
    private val repo: RepositoryDataAnggota
) : ViewModel() {
    var editUiState by mutableStateOf(UIStateAnggota())
        private set

    private val idAnggota: Int =
        checkNotNull(
        savedStateHandle[DestinasiAnggotaEdit.itemIdArg] ?: 0
    ) {
        "idAnggota tidak ditemukan di SavedStateHandle"
    }

    init {
        viewModelScope.launch {
            try {
                val anggota = repo.getAnggotaById(idAnggota)
                editUiState = anggota.toUiStateAnggota(cekValidasi(anggota.toDetailAnggota()))
            } catch (e: Exception) { /* Handle error */ }
        }
    }

    private fun cekValidasi(detail: DetailAnggota): Boolean {
        return detail.nama.isNotBlank() && detail.nim.length == 11 && detail.divisi.isNotBlank()
    }

    fun updateUiState(detailAnggota: DetailAnggota) {
        editUiState = UIStateAnggota(
            detailAnggota = detailAnggota,
            isEntryValid = cekValidasi(detailAnggota)
        )
    }

    suspend fun updateAnggota(): Boolean {
        return try {
            val response = repo.updateAnggota(idAnggota, editUiState.detailAnggota.toDataAnggota())
            response.isSuccessful
        } catch (e: Exception) { false }
    }
}