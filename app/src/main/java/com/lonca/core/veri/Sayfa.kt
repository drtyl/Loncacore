package com.lonca.core.veri

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Laboratuvar'da üretilen bir proje — tek bir "sayfa" değil, gerçek bir
 * dosya/klasör ağacının (bkz. Dosya.kt) sahibi. girisDosyaYolu, kum
 * havuzunun çalıştırmaya başlayacağı dosya (genelde "index.html").
 */
@Entity(tableName = "sayfalar")
data class Sayfa(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isim: String,
    val girisDosyaYolu: String = "index.html",
    val olusturmaTarihi: Long = System.currentTimeMillis()
)
