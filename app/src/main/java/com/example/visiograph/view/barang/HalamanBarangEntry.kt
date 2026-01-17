package com.example.visiograph.view.barang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visiograph.R
import com.example.visiograph.modeldata.DetailBarang
import com.example.visiograph.view.component.TopAppBar
import com.example.visiograph.viewmodel.PenyediaViewModel
import com.example.visiograph.viewmodel.barang.EntryBarangVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBarangScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryBarangVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Tambah Barang",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBarangBody(
            uiStateBarang = viewModel.uiStateBarang,
            onBarangValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.addBarang()) {
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
fun EntryBarangBody(
    uiStateBarang: com.example.visiograph.modeldata.UIStateBarang,
    onBarangValueChange: (DetailBarang) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navyGray = colorResource(id = R.color.navy_gray)

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .background(colorResource(id = R.color.light_gray))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            Text("Simpan Data Barang", fontWeight = FontWeight.Bold, color = colorResource(R.color.white))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBarang(
    detailBarang: DetailBarang,
    modifier: Modifier = Modifier,
    onValueChange: (DetailBarang) -> Unit = {},
    enabled: Boolean = true
) {
    val kategoriOptions = listOf("Kamera & Optik", "Audio & Recording", "Lighting", "Stabilisasi", "Aksesoris Tambahan")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = detailBarang.nama,
            onValueChange = { onValueChange(detailBarang.copy(nama = it)) },
            label = { Text("Nama Barang") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = detailBarang.kategori,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth(),
                enabled = enabled,
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                kategoriOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(detailBarang.copy(kategori = option))
                            expanded = false
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jumlah Barang",
                fontSize = 14.sp,
                color = colorResource(R.color.light_gray),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledIconButton(
                    onClick = { onValueChange(detailBarang.copy(jumlah_total = detailBarang.jumlah_total - 1)) },
                    enabled = enabled && detailBarang.jumlah_total > 0,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colorResource(id = R.color.navy_gray)
                    )
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }

                Text(
                    text = detailBarang.jumlah_total.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                FilledIconButton(
                    onClick = { onValueChange(detailBarang.copy(jumlah_total = detailBarang.jumlah_total + 1)) },
                    enabled = enabled,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colorResource(id = R.color.navy_gray)
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}