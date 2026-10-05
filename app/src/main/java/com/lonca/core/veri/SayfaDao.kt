package com.lonca.core.veri

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SayfaDao {
    @Query("SELECT * FROM sayfalar ORDER BY olusturmaTarihi DESC")
    fun hepsiniGetir(): Flow<List<Sayfa>>

    @Query("SELECT * FROM sayfalar WHERE id = :id")
    suspend fun idIleGetir(id: Long): Sayfa?

    @Insert
    suspend fun ekle(sayfa: Sayfa): Long

    @Query("UPDATE sayfalar SET girisDosyaYolu = :girisDosyaYolu WHERE id = :id")
    suspend fun girisDosyasiniGuncelle(id: Long, girisDosyaYolu: String)

    @Delete
    suspend fun sil(sayfa: Sayfa)
}
