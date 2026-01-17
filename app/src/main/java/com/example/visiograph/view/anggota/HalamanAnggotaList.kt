package com.example.visiograph.view.anggota

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visiograph.R
import com.example.visiograph.modeldata.DataAnggota
import com.example.visiograph.view.component.SearchBarCustom
import com.example.visiograph.view.component.TopAppBar
import com.example.visiograph.viewmodel.PenyediaViewModel
import com.example.visiograph.viewmodel.anggota.ListAnggotaUiState
import com.example.visiograph.viewmodel.anggota.ListAnggotaVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAnggotaScreen(
    navigateToItemEntry: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListAnggotaVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val navyGray = colorResource(id = R.color.navy_gray)
    val lightGray = colorResource(id = R.color.light_gray)
    val searchText by viewModel.searchText.collectAsState()
    val filteredList by viewModel.filteredAnggota.collectAsState()
    val selectedDivisi by viewModel.selectedDivisi.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Daftar Anggota",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                containerColor = navyGray,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Anggota")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(lightGray)
        ) {
            // --- Bagian Search & Filter ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(navyGray)
                    .padding(16.dp)
            ) {
                // Search Field
                SearchBarCustom(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChange(it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Divisi (Horizontal Scroll)
                val divisiOptions = listOf("All", "Fotografi", "Videografi", "Desain & Publikasi")
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    divisiOptions.forEach { divisi ->
                        val isSelected = selectedDivisi == divisi
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onDivisiChange(divisi) },
                            label = { Text(divisi) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = navyGray,
                                labelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = Color.White,               // Warna garis saat tidak dipilih
                                selectedBorderColor = Color.Transparent,  // Hilangkan garis saat chip dipilih (karena sudah jadi putih)
                                borderWidth = 1.dp
                            )
                        )
                    }
                }
            }

            // --- List Data ---
            when (val state = viewModel.anggotaUiState) {
                is ListAnggotaUiState.Loading -> LoadingScreen()
                is ListAnggotaUiState.Error -> ErrorScreen(retryAction = viewModel::getAnggota)
                is ListAnggotaUiState.Success -> {
                    if (filteredList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Data tidak ditemukan")
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = filteredList,
                                key = { it.id }
                            ) { anggota ->
                                ItemAnggota(
                                    anggota = anggota,
                                    onClick = { navigateToDetail(anggota.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemAnggota(anggota: DataAnggota, onClick: () -> Unit) {
    val navyGray = colorResource(id = R.color.navy_gray)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = navyGray,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = anggota.nama, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = navyGray)
                Text(text = "NIM: ${anggota.nim}", fontSize = 14.sp, color = Color.Gray)
                Text(text = anggota.divisi, fontSize = 12.sp, color = navyGray.copy(alpha = 0.7f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = stringResource(R.string.gagal), modifier = Modifier
            .padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}