package com.example.visiograph.uicontroller.route.anggota

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi
import com.example.visiograph.uicontroller.route.anggota.DestinasiAnggotaDetail.route

object DestinasiAnggotaEdit : DestinasiNavigasi {
    override val route = "anggota_edit"
    override val titleRes =  R.string.anggota_edit

    const val itemIdArg = "idAnggota"
    val routeWithArgs = "$route/{$itemIdArg}"
}