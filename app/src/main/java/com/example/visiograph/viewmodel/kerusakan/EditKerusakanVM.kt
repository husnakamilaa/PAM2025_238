package com.example.visiograph.viewmodel.kerusakan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataBarang
import com.example.visiograph.modeldata.DetailKerusakan
import com.example.visiograph.modeldata.UIStateKerusakan
import com.example.visiograph.modeldata.toDataKerusakan
import com.example.visiograph.modeldata.toDetailKerusakan
import com.example.visiograph.modeldata.toUiStateKerusakan
import com.example.visiograph.repositori.RepositoryDataBarang
import com.example.visiograph.repositori.RepositoryDataKerusakan
import com.example.visiograph.uicontroller.route.kerusakan.DestinasiKerusakanEdit
import kotlinx.coroutines.launch

class EditKerusakanVM(
    savedStateHandle: SavedStateHandle,
    private val repoKerusakan: RepositoryDataKerusakan,
    private val repoBarang: RepositoryDataBarang
) : ViewModel() {

    // Ambil ID dari argument navigasi
    private val idKerusakan: Int = checkNotNull(savedStateHandle[DestinasiKerusakanEdit.itemIdArg] ?: 0)

    var editUiState by mutableStateOf(UIStateKerusakan())
        private set

    var daftarBarang by mutableStateOf(listOf<DataBarang>())
        private set

    init {
        viewModelScope.launch {
            try {
                // Ambil data dropdown barang (integrasi)
                daftarBarang = repoBarang.getAllBarang()

                // Ambil data kerusakan lama yang mau diedit
                val kerusakan = repoKerusakan.getKerusakanById(idKerusakan)
                editUiState = kerusakan.toUiStateKerusakan(cekValidasi(kerusakan.toDetailKerusakan()))
            } catch (e: Exception) {
                println("DEBUG_VM: Gagal load data edit kerusakan: ${e.message}")
            }
        }
    }

    private fun cekValidasi(detail: DetailKerusakan): Boolean {
        // Validasi: Barang terpilih, deskripsi ada isi, dan jumlah tidak 0/minus
        return detail.id_barang != 0 && detail.deskripsi.isNotBlank() && detail.jumlah > 0
    }

    fun updateUiState(detail: DetailKerusakan) {
        editUiState = UIStateKerusakan(
            detailKerusakan = detail,
            isEntryValid = cekValidasi(detail)
        )
    }

    suspend fun updateKerusakan(): Boolean {
        return try {
            val response = repoKerusakan.updateKerusakan(
                idKerusakan,
                editUiState.detailKerusakan.toDataKerusakan()
            )
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}