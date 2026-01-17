package com.example.visiograph.viewmodel.kerusakan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataBarang
import com.example.visiograph.modeldata.DetailKerusakan
import com.example.visiograph.modeldata.UIStateKerusakan
import com.example.visiograph.modeldata.toDataKerusakan
import com.example.visiograph.repositori.RepositoryDataBarang
import com.example.visiograph.repositori.RepositoryDataKerusakan
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntryKerusakanVM(
    private val repoKerusakan: RepositoryDataKerusakan,
    private val repoBarang: RepositoryDataBarang
) : ViewModel() {

    var uiStateKerusakan by mutableStateOf(UIStateKerusakan())
        private set

    var daftarBarang by mutableStateOf(listOf<DataBarang>())
        private set

    init {
        ambilDaftarBarang()
    }

    private fun ambilDaftarBarang() {
        viewModelScope.launch {
            try {
                daftarBarang = repoBarang.getAllBarang()
            } catch (e: Exception) {
                // Log error jika diperlukan
            }
        }
    }

    fun updateUiState(detail: DetailKerusakan) {
        uiStateKerusakan = UIStateKerusakan(
            detailKerusakan = detail,
            isEntryValid = validasiInput(detail)
        )
    }

    private fun validasiInput(detail: DetailKerusakan): Boolean {
        return with(detail) {
            val barangDipilih = daftarBarang.find { it.id == id_barang }
            val stokTersedia = barangDipilih?.jumlah_total ?: 0
            id_barang != 0 &&
                    deskripsi.isNotBlank() &&
                    jumlah > 0 &&
                    jumlah <= stokTersedia
        }
    }

    suspend fun addKerusakan(): Boolean {
        // Ambil tanggal saat ini (current date)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tanggalSekarang = sdf.format(Date())

        // Set tanggal otomatis dan status default "Belum"
        val dataFinal = uiStateKerusakan.detailKerusakan.toDataKerusakan().copy(
            tanggal = tanggalSekarang,
            status_perbaikan = "Belum"
        )

        return try {
            val response = repoKerusakan.createKerusakan(dataFinal)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}