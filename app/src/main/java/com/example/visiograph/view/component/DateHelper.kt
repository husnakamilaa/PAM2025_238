package com.example.visiograph.view.component

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun formatTanggalIndonesia(tanggal: String): String {
    if (tanggal.isBlank()) return "-"
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val date = parser.parse(tanggal)
        formatter.format(date!!)
    } catch (e: Exception) {
        tanggal
    }
}