package com.example.visiograph.uicontroller.route.peminjaman

import com.example.visiograph.R
import com.example.visiograph.uicontroller.route.DestinasiNavigasi

object DestinasiPeminjamanEdit : DestinasiNavigasi {
    override val route = "peminjaman_edit"
    override val titleRes = R.string.peminjaman_edit

    const val itemIdArg = "idPeminjaman"
    val routeWithArgs = "${route}/{$itemIdArg}"
}