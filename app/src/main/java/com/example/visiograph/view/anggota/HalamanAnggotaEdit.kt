package com.example.visiograph.view.anggota

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visiograph.R
import com.example.visiograph.modeldata.DetailAnggota
import com.example.visiograph.modeldata.UIStateAnggota
import com.example.visiograph.view.component.TopAppBar
import com.example.visiograph.viewmodel.PenyediaViewModel
import com.example.visiograph.viewmodel.anggota.EditAnggotaVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAnggotaScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditAnggotaVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Edit Anggota",
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        EditAnggotaBody(
            uiStateAnggota = viewModel.editUiState,
            onAnggotaValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.updateAnggota()) {
                        navigateBack()
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        )
    }
}

@Composable
fun EditAnggotaBody(
    uiStateAnggota: UIStateAnggota,
    onAnggotaValueChange: (DetailAnggota) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val navyGray = colorResource(id = R.color.navy_gray)

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .background(colorResource(id = R.color.light_gray))
            .padding(24.dp)
    ) {
        FormAnggota(
            detailAnggota = uiStateAnggota.detailAnggota,
            onValueChange = onAnggotaValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            enabled = uiStateAnggota.isEntryValid, // Validasi NIM dan Nama
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = navyGray),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Perbarui Data", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}