package com.lonca.core.kumhavuzu

import android.content.Context
import android.webkit.MimeTypeMap
import android.webkit.WebResourceResponse
import androidx.webkit.WebViewAssetLoader
import com.lonca.core.veri.Dosya
import com.lonca.core.veri.DosyaDao
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

/** Her projenin ikili (resim, font vb.) dosyalarının cihazda durduğu klasör. */
fun sayfaIkiliKlasoru(context: Context, sayfaId: Long): File {
    val klasor = File(context.filesDir, "sayfa_$sayfaId")
    if (!klasor.exists()) klasor.mkdirs()
    return klasor
}

private fun mimeTuruTahminEt(yol: String): String {
    val uzanti = yol.substringAfterLast('.', "").lowercase()
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(uzanti) ?: "application/octet-stream"
}

/**
 * Bir projenin dosyalarını WebView'a sunan "sahte sunucu". Gerçek bir
 * web sunucusu gibi davranıyor: WebView bir dosya isteyince (örn.
 * "css/style.css"), burada Room'dan (metin) ya da cihaz deposundan
 * (ikili) okuyup döndürüyoruz — bu sayede <img src="...">, <link
 * href="..."> gibi göreceli yollar GERÇEKTEN çalışıyor.
 */
class SayfaDosyaIsleyici(
    private val sayfaId: Long,
    private val context: Context,
    private val dosyaDao: DosyaDao
) : WebViewAssetLoader.PathHandler {
    override fun handle(path: String): WebResourceResponse? {
        val dosya: Dosya = dosyaDao.yolIleGetirSenkron(sayfaId, path) ?: return null
        val mime = mimeTuruTahminEt(path)

        return if (dosya.icerikTuru == "metin") {
            val icerik = dosya.icerikMetin ?: ""
            WebResourceResponse(mime, "UTF-8", ByteArrayInputStream(icerik.toByteArray(Charsets.UTF_8)))
        } else {
            val gercekDosya = File(sayfaIkiliKlasoru(context, sayfaId), path)
            if (!gercekDosya.exists()) return null
            WebResourceResponse(mime, null, FileInputStream(gercekDosya))
        }
    }
}

const val SAYFA_SUNUCU_ALAN_ADI = "appassets.androidplatform.net"
const val SAYFA_SUNUCU_ONEKI = "/sayfa/"

fun sayfaIcinAssetLoaderOlustur(sayfaId: Long, context: Context, dosyaDao: DosyaDao): WebViewAssetLoader {
    return WebViewAssetLoader.Builder()
        .setDomain(SAYFA_SUNUCU_ALAN_ADI)
        .addPathHandler(SAYFA_SUNUCU_ONEKI, SayfaDosyaIsleyici(sayfaId, context, dosyaDao))
        .build()
}

fun sayfaCalistirmaAdresi(girisDosyaYolu: String): String {
    return "https://$SAYFA_SUNUCU_ALAN_ADI$SAYFA_SUNUCU_ONEKI$girisDosyaYolu"
}
