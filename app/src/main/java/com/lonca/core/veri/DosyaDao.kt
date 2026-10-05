package com.lonca.core.veri

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DosyaDao {
    @Query("SELECT * FROM dosyalar WHERE sayfaId = :sayfaId ORDER BY yol ASC")
    fun sayfayaGoreGetir(sayfaId: Long): Flow<List<Dosya>>

    @Query("SELECT * FROM dosyalar WHERE id = :id")
    suspend fun idIleGetir(id: Long): Dosya?

    @Query("SELECT * FROM dosyalar WHERE sayfaId = :sayfaId AND yol = :yol LIMIT 1")
    suspend fun yolIleGetir(sayfaId: Long, yol: String): Dosya?

    /** WebViewAssetLoader'ın arka plan iş parçacığından, askıya almadan (senkron) çağırdığı sürüm. */
    @Query("SELECT * FROM dosyalar WHERE sayfaId = :sayfaId AND yol = :yol LIMIT 1")
    fun yolIleGetirSenkron(sayfaId: Long, yol: String): Dosya?

    @Insert
    suspend fun ekle(dosya: Dosya): Long

    @Query("UPDATE dosyalar SET icerikMetin = :icerikMetin, boyut = :boyut WHERE id = :id")
    suspend fun metinGuncelle(id: Long, icerikMetin: String, boyut: Long)

    @Delete
    suspend fun sil(dosya: Dosya)

    @Query("DELETE FROM dosyalar WHERE sayfaId = :sayfaId")
    suspend fun sayfayaGoreSilHepsi(sayfaId: Long)
}
