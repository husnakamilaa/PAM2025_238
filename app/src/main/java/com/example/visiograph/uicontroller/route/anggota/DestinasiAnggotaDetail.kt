package com.example.visiograph.uicontroller.route.anggota

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi

object DestinasiAnggotaDetail: DestinasiNavigasi {
    override val route = "anggota_detail"
    override val titleRes = R.string.anggota_detail

    const val itemIdArg = "idAnggota"
    val routeWithArgs = "$route/{$itemIdArg}"
}