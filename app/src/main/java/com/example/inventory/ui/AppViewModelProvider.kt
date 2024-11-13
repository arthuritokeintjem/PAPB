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

package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.InventoryApplication
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.item.ItemDetailsViewModel
import com.example.inventory.ui.item.ItemEditViewModel
import com.example.inventory.ui.item.ItemEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 * Kelas ini menyediakan Factory untuk membuat instance dari ViewModel yang digunakan di seluruh aplikasi Inventory.
 * Setiap ViewModel memiliki inisialisasi khusus untuk memenuhi kebutuhan masing-masing.
 */
object AppViewModelProvider {
    // Factory untuk membuat ViewModel
    val Factory = viewModelFactory {
        // Initializer untuk ItemEditViewModel
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle() // Menggunakan saved state untuk menangani status dan data terkait ViewModel.
            )
        }
        // Initializer untuk ItemEntryViewModel
        initializer {
            // Membuat instance ItemEntryViewModel dengan mengambil repository dari container aplikasi.
            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
        }

        // Initializer untuk ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle() // Menggunakan saved state untuk menangani status dan data terkait ViewModel.
            )
        }

        // Initializer untuk HomeViewModel
        initializer {
            HomeViewModel()
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): InventoryApplication =
    // Mengambil objek [Application] dari [CreationExtras] menggunakan key
    // yang sudah ditentukan sebelumnya dalam [AndroidViewModelFactory.APPLICATION_KEY].
    // Kunci ini digunakan untuk menyimpan dan mengakses aplikasi yang sedang berjalan,
    // khususnya yang bertipe [InventoryApplication].
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)