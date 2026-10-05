package com.lonca.core.veri

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Bir projenin (Sayfa) İÇİNDEKİ tek bir dosya. Klasörler ayrı bir kayıt
 * DEĞİL — "yol" alanının kendisi ("gorseller/logo.png" gibi) klasör
 * yapısını zaten taşıyor, ekranlar bunu yol'a göre gruplayıp gösteriyor.
 *
 * icerikTuru "metin" ise icerikMetin'de saklanır (Room/SQLite içinde).
 * icerikTuru "ikili" ise (resim, font, vb.) gerçek baytlar Room'da DEĞİL,
 * cihazın kendi dosya sisteminde duruyor — Room sadece "nerede olduğunu"
 * biliyor (yol üzerinden, bkz. kumhavuzu/SayfaSunucusu.kt).
 */
@Entity(tableName = "dosyalar")
data class Dosya(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sayfaId: Long,
    val yol: String,
    val icerikTuru: String, // "metin" | "ikili"
    val icerikMetin: String? = null,
    val boyut: Long = 0,
    val olusturmaTarihi: Long = System.currentTimeMillis()
)
