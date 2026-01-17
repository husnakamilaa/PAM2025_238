package com.example.visiograph.view.anggota

import EntryAnggotaVM
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visiograph.R
import com.example.visiograph.modeldata.DetailAnggota
import com.example.visiograph.modeldata.UIStateAnggota
import com.example.visiograph.view.component.TopAppBar
import com.example.visiograph.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryAnggotaScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryAnggotaVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Tambah Anggota",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryAnggotaBody(
            uiStateAnggota = viewModel.uiStateAnggota,
            onAnggotaValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.addAnggota()) {
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
fun EntryAnggotaBody(
    uiStateAnggota: UIStateAnggota,
    onAnggotaValueChange: (DetailAnggota) -> Unit,
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
        FormAnggota(
            detailAnggota = uiStateAnggota.detailAnggota,
            onValueChange = onAnggotaValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            enabled = uiStateAnggota.isEntryValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = navyGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Simpan Data", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAnggota(
    detailAnggota: DetailAnggota,
    modifier: Modifier = Modifier.fillMaxSize(),
    onValueChange: (DetailAnggota) -> Unit = {},
    enabled: Boolean = true
) {
    val divisiOptions = listOf("Fotografi", "Videografi", "Desain & Publikasi")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nama Lengkap
        OutlinedTextField(
            value = detailAnggota.nama,
            onValueChange = { onValueChange(detailAnggota.copy(nama = it)) },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // NIM
        OutlinedTextField(
            value = detailAnggota.nim,
            onValueChange = { if (it.length <= 11) onValueChange(detailAnggota.copy(nim = it)) },
            label = { Text("NIM") },
            placeholder = { Text("Wajib 11 Digit") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (detailAnggota.nim.isNotEmpty() && detailAnggota.nim.length != 11) {
                    Text("NIM harus 11 digit!", color = Color.Red)
                }
            }
        )

        // Dropdown Divisi
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = detailAnggota.divisi,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Divisi") },
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
                divisiOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(detailAnggota.copy(divisi = option))
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}