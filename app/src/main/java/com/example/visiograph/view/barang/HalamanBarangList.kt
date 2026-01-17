package com.example.visiograph.view.barang

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
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visiograph.R
import com.example.visiograph.modeldata.DataBarang
import com.example.visiograph.view.component.SearchBarCustom
import com.example.visiograph.view.component.TopAppBar
import com.example.visiograph.view.anggota.ErrorScreen
import com.example.visiograph.view.anggota.LoadingScreen
import com.example.visiograph.viewmodel.PenyediaViewModel
import com.example.visiograph.viewmodel.barang.ListBarangUiState
import com.example.visiograph.viewmodel.barang.ListBarangVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBarangScreen(
    navigateToItemEntry: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListBarangVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val navyGray = colorResource(id = R.color.navy_gray)
    val lightGray = colorResource(id = R.color.light_gray)

    val searchText by viewModel.searchText.collectAsState()
    val filteredList by viewModel.filteredBarang.collectAsState()
    val selectedKategori by viewModel.selectedKategori.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Daftar Barang",
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
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(lightGray)
        ) {

            // ===== SEARCH & FILTER =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(navyGray)
                    .padding(16.dp)
            ) {
                SearchBarCustom(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChange(it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                val kategoriOptions = listOf(
                    "All",
                    "Kamera & Optik",
                    "Audio & Recording",
                    "Lighting",
                    "Stabilisasi",
                    "Aksesoris tambahan"
                )

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    kategoriOptions.forEach { kategori ->
                        val selected = selectedKategori == kategori
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.onKategoriChange(kategori) },
                            label = { Text(kategori) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = navyGray,
                                labelColor = Color.White,
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = Color.White,
                                selectedBorderColor = Color.Black
                            )
                        )
                    }
                }
            }

            // ===== LIST DATA =====
            when (viewModel.barangUiState) {
                is ListBarangUiState.Loading -> LoadingScreen()
                is ListBarangUiState.Error -> ErrorScreen(retryAction = viewModel::getBarang)
                is ListBarangUiState.Success -> {
                    if (filteredList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Data barang tidak ditemukan")
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = filteredList,
                                key = { it.id }
                            ) { dataBarang ->
                                ItemBarang(
                                    barang = dataBarang,
                                    onClick = { navigateToDetail(dataBarang.id) }
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
fun ItemBarang(
    barang: DataBarang,
    onClick: () -> Unit
) {
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
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = navyGray,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(barang.nama, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = navyGray)
                Text("Kategori: ${barang.kategori}", fontSize = 14.sp, color = Color.Gray)
                Text("Stok: ${barang.jumlah_total}", fontSize = 12.sp, color = navyGray.copy(0.7f))
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}