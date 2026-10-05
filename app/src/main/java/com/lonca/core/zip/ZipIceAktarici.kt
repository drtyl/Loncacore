package com.lonca.core.zip

import android.content.Context
import android.net.Uri
import com.lonca.core.kumhavuzu.sayfaIkiliKlasoru
import com.lonca.core.veri.Dosya
import com.lonca.core.veri.DosyaDao
import com.lonca.core.veri.Sayfa
import com.lonca.core.veri.SayfaDao
import java.io.File
import java.util.zip.ZipInputStream

private val METIN_UZANTILARI = setOf("html", "htm", "css", "js", "json", "xml", "txt", "md", "svg")

fun dosyaMetinMi(yol: String): Boolean {
    val uzanti = yol.substringAfterLast('.', "").lowercase()
    return uzanti in METIN_UZANTILARI
}

/**
 * Seçilen bir zip dosyasını okuyup YENİ bir proje (Sayfa) olarak içe
 * aktarır. Metin dosyaları (html/css/js/json/...) Room'a, ikili
 * dosyalar (resim, font, vb.) cihazın kendi deposuna yazılır. "index.html"
 * yoksa, zip içinde bulunan ilk .html dosyası giriş noktası yapılır.
 *
 * @return yeni oluşturulan Sayfa'nın id'si
 */
suspend fun zipIceAktar(
    context: Context,
    uri: Uri,
    projeAdi: String,
    sayfaDao: SayfaDao,
    dosyaDao: DosyaDao
): Long {
    val sayfaId = sayfaDao.ekle(Sayfa(isim = projeAdi, girisDosyaYolu = "index.html"))
    val ikiliKlasor = sayfaIkiliKlasoru(context, sayfaId)

    var girisBulunduMu = false
    var ilkHtmlYolu: String? = null

    context.contentResolver.openInputStream(uri)?.use { girisAkisi ->
        ZipInputStream(girisAkisi).use { zipGiris ->
            var girdi = zipGiris.nextEntry
            while (girdi != null) {
                if (!girdi.isDirectory) {
                    val yol = girdi.name.trimStart('/')
                    if (yol.isNotBlank()) {
                        if (dosyaMetinMi(yol)) {
                            val icerik = zipGiris.readBytes().toString(Charsets.UTF_8)
                            dosyaDao.ekle(
                                Dosya(sayfaId = sayfaId, yol = yol, icerikTuru = "metin", icerikMetin = icerik, boyut = icerik.length.toLong())
                            )
                            if (yol.equals("index.html", ignoreCase = true)) girisBulunduMu = true
                            if (ilkHtmlYolu == null && yol.endsWith(".html", ignoreCase = true)) ilkHtmlYolu = yol
                        } else {
                            val baytlar = zipGiris.readBytes()
                            val hedefDosya = File(ikiliKlasor, yol)
                            hedefDosya.parentFile?.mkdirs()
                            hedefDosya.writeBytes(baytlar)
                            dosyaDao.ekle(
                                Dosya(sayfaId = sayfaId, yol = yol, icerikTuru = "ikili", boyut = baytlar.size.toLong())
                            )
                        }
                    }
                }
                zipGiris.closeEntry()
                girdi = zipGiris.nextEntry
            }
        }
    }

    val bulunanIlkHtml = ilkHtmlYolu
    if (!girisBulunduMu && bulunanIlkHtml != null) {
        sayfaDao.girisDosyasiniGuncelle(sayfaId, bulunanIlkHtml)
    }

    return sayfaId
}
