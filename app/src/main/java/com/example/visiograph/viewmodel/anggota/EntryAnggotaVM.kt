import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.visiograph.modeldata.DetailAnggota
import com.example.visiograph.modeldata.UIStateAnggota
import com.example.visiograph.modeldata.toDataAnggota
import com.example.visiograph.repositori.RepositoryDataAnggota
import retrofit2.Response

class EntryAnggotaVM(
    private val repo: RepositoryDataAnggota
) : ViewModel() {

    var uiStateAnggota by mutableStateOf(UIStateAnggota())
        private set

    private fun validasiInput(detail: DetailAnggota = uiStateAnggota.detailAnggota): Boolean {
        return with(detail) {
            nama.isNotBlank() &&
                    nim.length == 11 && // Syarat 11 digit NIM
                    divisi.isNotBlank()
        }
    }

    fun updateUiState(detailAnggota: DetailAnggota) {
        uiStateAnggota = UIStateAnggota(
            detailAnggota = detailAnggota,
            isEntryValid = validasiInput(detailAnggota)
        )
    }

    suspend fun addAnggota(): Boolean {
        return if (validasiInput()) {
            try {
                val response = repo.createAnggota(uiStateAnggota.detailAnggota.toDataAnggota())
                response.isSuccessful
            } catch (e: Exception) {
                println("Error Save Anggota: ${e.message}")
                false
            }
        } else {
            false
        }
    }
}