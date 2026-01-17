package com.example.visiograph.viewmodel.peminjaman

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DataAnggota
import com.example.visiograph.modeldata.DataBarang
import com.example.visiograph.modeldata.DetailPeminjaman
import com.example.visiograph.modeldata.UIStatePeminjaman
import com.example.visiograph.modeldata.toDataPeminjaman
import com.example.visiograph.repositori.RepositoryDataAnggota
import com.example.visiograph.repositori.RepositoryDataBarang
import com.example.visiograph.repositori.RepositoryDataPeminjaman
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class EntryPeminjamanVM(
    private val repoPinjam: RepositoryDataPeminjaman,
    private val repoBarang: RepositoryDataBarang,
    private val repoAnggota: RepositoryDataAnggota
) : ViewModel() {

    var uiStatePeminjaman by mutableStateOf(UIStatePeminjaman())
        private set

    // State untuk Dropdown
    var daftarBarang by mutableStateOf(listOf<DataBarang>())
    var daftarAnggota by mutableStateOf(listOf<DataAnggota>())

    init {
        ambilDataDropdown()
    }

    private fun ambilDataDropdown() {
        viewModelScope.launch {
            try {
                daftarBarang = repoBarang.getAllBarang()
                daftarAnggota = repoAnggota.getAllAnggota()
            } catch (e: Exception) { /* log error */ }
        }
    }

    private fun validasiInput(detail: DetailPeminjaman = uiStatePeminjaman.detailPeminjaman): Boolean {
        return with(detail) {
            id_barang != 0 && id_anggota != 0 && jumlah_pinjam > 0 &&
                    tanggal_pinjam.isNotBlank() && tanggal_kembali.isNotBlank() && validasiTanggal(tanggal_pinjam)
        }
    }

    private fun validasiTanggal(tglPinjam: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val datePinjam = sdf.parse(tglPinjam)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        return !datePinjam.before(today)
    }

    fun updateUiState(detail: DetailPeminjaman) {
        uiStatePeminjaman = UIStatePeminjaman(
            detailPeminjaman = detail,
            isEntryValid = validasiInput(detail)
        )
    }

    suspend fun addPeminjaman(): Boolean {
        return try {
            val response = repoPinjam.insertPeminjaman(uiStatePeminjaman.detailPeminjaman.toDataPeminjaman())
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}