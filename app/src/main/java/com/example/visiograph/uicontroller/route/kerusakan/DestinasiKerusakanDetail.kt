package com.example.visiograph.uicontroller.route.kerusakan

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi

object DestinasiKerusakanDetail: DestinasiNavigasi {
    override val route = "kerusakan_detail"
    override val titleRes = R.string.kerusakan_detail

    const val itemIdArg = "idKerusakan"
    val routeWithArgs = "$route/{$itemIdArg}"
}