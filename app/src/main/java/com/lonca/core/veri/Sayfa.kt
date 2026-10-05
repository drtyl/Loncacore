package com.lonca.core.veri

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Laboratuvar'da üretilip "Sisteme Eklenen" bir sayfa/araç.
 * İçerik (HTML/CSS/JS), tek bir metin olarak saklanıyor — kum havuzu
 * (WebView) bunu doğrudan çalıştırıyor.
 */
@Entity(tableName = "sayfalar")
data class Sayfa(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isim: String,
    val icerik: String,
    val olusturmaTarihi: Long = System.currentTimeMillis()
)
