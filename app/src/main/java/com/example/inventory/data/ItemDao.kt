package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Dao digunakan untuk mengakses database.
@Dao
interface ItemDao {
    // Fungsi untuk menyisipkan item ke dalam database. Jika ada konflik (misalnya item dengan ID yang sama), maka penyisipan akan diabaikan.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    // Fungsi untuk memperbarui data item yang sudah ada di dalam database.
    @Update
    suspend fun update(item: Item)

    // Fungsi untuk menghapus data item dari database.
    @Delete
    suspend fun delete(item: Item)

    // Fungsi untuk mengambil data item berdasarkan ID dari database.
    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // Fungsi untuk mengambil semua data item dari database dan mengurutkannya berdasarkan nama secara ascending (A-Z).
    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
}