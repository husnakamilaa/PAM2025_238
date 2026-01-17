package com.example.visiograph.viewmodel.peminjaman

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataAnggota
import com.example.visiograph.modeldata.DataBarang
import com.example.visiograph.modeldata.DetailPeminjaman
import com.example.visiograph.modeldata.UIStatePeminjaman
import com.example.visiograph.modeldata.toDataPeminjaman
import com.example.visiograph.modeldata.toDetailPeminjaman
import com.example.visiograph.modeldata.toUiStatePeminjaman
import com.example.visiograph.repositori.RepositoryDataAnggota
import com.example.visiograph.repositori.RepositoryDataBarang
import com.example.visiograph.repositori.RepositoryDataPeminjaman
import com.example.visiograph.uicontroller.route.peminjaman.DestinasiPeminjamanEdit
import kotlinx.coroutines.launch

class EditPeminjamanVM(
    savedStateHandle: SavedStateHandle,
    private val repoPinjam: RepositoryDataPeminjaman,
    private val repoBarang: RepositoryDataBarang,
    private val repoAnggota: RepositoryDataAnggota
) : ViewModel() {

    private val idPinjam: Int = checkNotNull(savedStateHandle[DestinasiPeminjamanEdit.itemIdArg] ?: 0)

    var editUiState by mutableStateOf(UIStatePeminjaman())
        private set

    var daftarBarang by mutableStateOf(listOf<DataBarang>())
    var daftarAnggota by mutableStateOf(listOf<DataAnggota>())

    init {
        viewModelScope.launch {
            try {
                // Ambil data dropdown
                daftarBarang = repoBarang.getAllBarang()
                daftarAnggota = repoAnggota.getAllAnggota()

                // Ambil data peminjaman yang mau diedit
                val pinjam = repoPinjam.getPeminjamanById(idPinjam)
                editUiState = pinjam.toUiStatePeminjaman(cekValidasi(pinjam.toDetailPeminjaman()))
            } catch (e: Exception) {
                println("DEBUG_VM: Gagal load data edit pinjam: ${e.message}")
            }
        }
    }

    private fun cekValidasi(detail: DetailPeminjaman): Boolean {
        return detail.id_barang != 0 && detail.id_anggota != 0 && detail.jumlah_pinjam > 0
    }

    fun updateUiState(detail: DetailPeminjaman) {
        editUiState = UIStatePeminjaman(
            detailPeminjaman = detail,
            isEntryValid = cekValidasi(detail)
        )
    }

    suspend fun updatePeminjaman(): Boolean {
        return try {
            val response = repoPinjam.updatePeminjaman(idPinjam, editUiState.detailPeminjaman.toDataPeminjaman())
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}