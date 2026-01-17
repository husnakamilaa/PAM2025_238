package com.example.visiograph.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class DataAnggota(
    val id: Int,
    val nama: String,
    val nim: String,
    val divisi: String
)

data class UIStateAnggota(
    val detailAnggota: DetailAnggota = DetailAnggota(),
    val isEntryValid: Boolean = false
)

data class DetailAnggota(
    val id: Int = 0,
    val nama: String = "",
    val nim: String = "",
    val divisi: String = ""
)

fun DetailAnggota.toDataAnggota(): DataAnggota = DataAnggota(
    id = id,
    nama = nama,
    nim = nim,
    divisi = divisi
)

fun DataAnggota.toUiStateAnggota(
    isEntryValid: Boolean = false
): UIStateAnggota = UIStateAnggota(
    detailAnggota = this.toDetailAnggota(),
    isEntryValid = isEntryValid
)

fun DataAnggota.toDetailAnggota(): DetailAnggota = DetailAnggota(
    id = id,
    nama = nama,
    nim = nim,
    divisi = divisi
)

//buat searchkidss
fun DataAnggota.doesMatchSearchQuery(query: String): Boolean {
    val matchingCombinations = listOf(
        nama,
        nim,
        "$nama $nim"
    )
    return matchingCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}
