package com.example.visiograph.uicontroller.route.kerusakan

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi

object DestinasiKerusakanEdit : DestinasiNavigasi {
    override val route = "kerusakan_edit"
    override val titleRes = R.string.kerusakan_edit

    const val itemIdArg = "idKerusakan"
    val routeWithArgs = "${route}/{$itemIdArg}"
}