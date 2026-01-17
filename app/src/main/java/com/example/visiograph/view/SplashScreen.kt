package com.example.visiograph.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.visiograph.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.navy_gray)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "visiograph",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
