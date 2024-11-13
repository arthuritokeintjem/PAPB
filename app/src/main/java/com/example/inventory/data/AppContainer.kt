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

package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 * Interface ini mendefinisikan kontainer aplikasi untuk melakukan injeksi dependensi,
 * yang menyediakan objek-objek yang diperlukan oleh aplikasi.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository // Properti untuk menyediakan repository item
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 * Kelas ini merupakan implementasi dari [AppContainer], yang menyediakan instance dari [OfflineItemsRepository].
 * Ini akan memberikan akses ke data item yang tersimpan secara lokal di Room Database.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     * Properti ini akan menginisialisasi [OfflineItemsRepository] yang menggunakan DAO dari
     * database untuk berinteraksi dengan data item di dalam Room Database.
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
}