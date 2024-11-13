/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

// Destinasi navigasi untuk ItemEntryScreen
object ItemEntryDestination : NavigationDestination {
    override val route = "item_entry" // Rute untuk navigasi ke halaman item entry
    override val titleRes = R.string.item_entry_title // Judul halaman
}

// Fungsi utama untuk tampilan layar entri item
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit, // Fungsi untuk navigasi kembali
    onNavigateUp: () -> Unit, // Fungsi untuk navigasi ke atas
    canNavigateBack: Boolean = true, // Status apakah bisa menavigasi kembali
    viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory) // Mengambil viewModel
) {
    val coroutineScope = rememberCoroutineScope()

    // Scaffold sebagai struktur dasar untuk layar, dengan AppBar dan konten di dalamnya
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        // Menyusun konten layar dengan padding dan kemampuan scroll vertikal
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState, // Mengupdate state saat nilai input berubah
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem() // Menyimpan item jika valid
                    navigateBack() // Navigasi kembali setelah simpan
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState()) // Menambahkan scroll vertikal
                .fillMaxWidth()
        )
    }
}

// Body utama untuk input form entri item
@Composable
fun ItemEntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit, // Fungsi untuk mengupdate detail item
    onSaveClick: () -> Unit, // Fungsi untuk menyimpan item
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        // Form input untuk item
        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        // Tombol untuk menyimpan item
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid, // Hanya aktif jika input valid
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action)) // Teks tombol simpan
        }
    }
}

// Form input untuk nama, harga, dan kuantitas item
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {}, // Fungsi untuk mengupdate item details
    enabled: Boolean = true // Status apakah form bisa diubah
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        // Input untuk nama item
        OutlinedTextField(
            value = itemDetails.name,
            onValueChange = { onValueChange(itemDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.item_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Input untuk harga item dengan simbol mata uang
        OutlinedTextField(
            value = itemDetails.price,
            onValueChange = { onValueChange(itemDetails.copy(price = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.item_price_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Input untuk kuantitas item
        OutlinedTextField(
            value = itemDetails.quantity,
            onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.quantity_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Pesan yang menunjukkan bahwa field harus diisi
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

// Preview untuk ItemEntryScreen
@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    InventoryTheme {
        ItemEntryBody(itemUiState = ItemUiState(
            ItemDetails(
                name = "Item name", price = "10.00", quantity = "5"
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}