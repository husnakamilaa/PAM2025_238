package com.example.visiograph.viewmodel.peminjaman

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visiograph.modeldata.DetailPeminjaman
import com.example.visiograph.modeldata.toDetailPeminjaman
import com.example.visiograph.repositori.RepositoryDataPeminjaman
import com.example.visiograph.uicontroller.route.peminjaman.DestinasiPeminjamanDetail
import kotlinx.coroutines.launch

class DetailPeminjamanVM(
    savedStateHandle: SavedStateHandle,
    private val repo: RepositoryDataPeminjaman
) : ViewModel() {

    private val pinjamId: Int = checkNotNull(savedStateHandle[DestinasiPeminjamanDetail.itemIdArg] ?: 0)

    var detailUiState by mutableStateOf(DetailPeminjaman())
        private set

    init {
        viewModelScope.launch {
            try {
                val data = repo.getPeminjamanById(pinjamId)
                detailUiState = data.toDetailPeminjaman()
            } catch (e: Exception) {
                println("ERROR_VM_PINJAM: ${e.message}")
            }
        }
    }

    suspend fun deletePeminjaman() {
        repo.deletePeminjaman(pinjamId)
    }
}