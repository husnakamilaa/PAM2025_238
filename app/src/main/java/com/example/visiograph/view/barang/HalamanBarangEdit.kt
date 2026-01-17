package com.example.visiograph.view.barang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visiograph.R
import com.example.visiograph.modeldata.DetailBarang
import com.example.visiograph.modeldata.UIStateBarang
import com.example.visiograph.view.component.TopAppBar
import com.example.visiograph.viewmodel.PenyediaViewModel
import com.example.visiograph.viewmodel.barang.EditBarangVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBarangScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditBarangVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Edit Barang",
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        EditBarangBody(
            uiStateBarang = viewModel.editUiState,
            onBarangValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.updateBarang()) {
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
fun EditBarangBody(
    uiStateBarang: UIStateBarang,
    onBarangValueChange: (DetailBarang) -> Unit,
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
        FormBarang(
            detailBarang = uiStateBarang.detailBarang,
            onValueChange = onBarangValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            enabled = uiStateBarang.isEntryValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = navyGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Perbarui Data Barang",
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white)
            )
        }
    }
}