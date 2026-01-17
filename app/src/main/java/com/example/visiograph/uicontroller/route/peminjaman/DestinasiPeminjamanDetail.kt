package com.example.visiograph.uicontroller.route.peminjaman

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi

object DestinasiPeminjamanDetail: DestinasiNavigasi {
    override val route = "peminjaman_detail"
    override val titleRes = R.string.peminjaman_detail

    const val itemIdArg = "idPeminjaman"
    val routeWithArgs = "$route/{$itemIdArg}"
}