package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    // Mendeklarasikan fungsi abstrak untuk akses ke DAO (ItemDao) yang akan digunakan untuk operasi database
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        // Fungsi untuk mendapatkan instance singleton dari database
        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .build() // Membangun database dengan nama "item_database"
                    .also { Instance = it } // Menyimpan instance pertama kali untuk penggunaan selanjutnya
            }
        }
    }
}