package com.example.visiograph.uicontroller.route.anggota

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi
import com.example.visiograph.uicontroller.route.anggota.DestinasiHome.route

object DestinasiAnggotaDetail: DestinasiNavigasi {
    override val route = "detail_siswa"
    override val titleRes = R.string.anggota_detail

    const val itemIdArg = "idAnggota"
    val routeWithArgs = "$route/{$itemIdArg}"
}