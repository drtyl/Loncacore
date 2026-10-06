package com.lonca.core.kumhavuzu

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.lonca.core.veri.DosyaDao

/**
 * Kum havuzu — Laboratuvar'da yazılan projelerin çalıştığı izole alan.
 *
 * Güvenlik ilkeleri (Mimari Vizyon Raporu, Bölüm 5 ile aynı):
 * - WebView'a SADECE SayfaDosyaIsleyici üzerinden, cihazın kendi
 *   Room/dosya deposundan gelen içerik sunulur — hiçbir zaman gerçek
 *   internete çıkmaz.
 * - allowFileAccess ve allowContentAccess kapalı — cihazın gerçek
 *   dosyalarına (bizim kendi "sahte sunucu"muz dışında) dokunamaz.
 * - Native tarafla TEK ek bağlantısı, "lonca" adında dar kapsamlı bir
 *   mesaj köprüsü (WebMessageListener) — eski, riskli
 *   addJavascriptInterface yöntemi BİLEREK kullanılmıyor.
 * - Faz 2'de köprü sadece basit bir "aldım" yanıtı veriyor; gerçek
 *   yetenekler sonraki fazlarda buraya eklenecek.
 */
fun kumHavuzuWebViewOlustur(context: Context, agIzniVar: Boolean = false): WebView {
    val webView = WebView(context)

    webView.settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = false
        allowFileAccess = false
        allowContentAccess = false
        // Faz 5 — izin sistemi: proje açıkça izinli olmadıkça (agIzniVar)
        // gerçek internete HİÇ çıkamaz. Projenin kendi dosyaları (HTML/CSS/
        // JS/resim) bu ayardan etkilenmiyor çünkü onlar hiç ağa çıkmıyor —
        // SayfaDosyaIsleyici tarafından doğrudan cihazdan sunuluyor.
        blockNetworkLoads = !agIzniVar
    }

    if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
        WebViewCompat.addWebMessageListener(
            webView,
            "lonca",
            setOf("*")
        ) { _, message, _, _, replyProxy ->
            val gelenMesaj = message.data ?: ""
            replyProxy.postMessage("Kabuk şunu aldı: $gelenMesaj")
        }
    }

    return webView
}

/**
 * Bir WebView'ı, belirli bir projenin (sayfaId) dosyalarını sunacak
 * şekilde bağlar ve giriş dosyasını yükler. kumHavuzuWebViewOlustur ile
 * birlikte kullanılır.
 */
fun sayfayiWebVieweYukle(webView: WebView, sayfaId: Long, girisDosyaYolu: String, context: Context, dosyaDao: DosyaDao) {
    val assetLoader = sayfaIcinAssetLoaderOlustur(sayfaId, context, dosyaDao)
    webView.webViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }
    webView.loadUrl(sayfaCalistirmaAdresi(girisDosyaYolu))
}

/** Yeni bir proje için çok yalın bir başlangıç — "index.html" dosyasının içeriği. */
const val ORNEK_INDEX_HTML = """<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
  body { background:#0B0B0F; color:#ECEDF0; font-family:sans-serif; padding:24px; }
  button { background:#4FF3E0; color:#0B0B0F; border:none; border-radius:10px; padding:10px 16px; font-weight:bold; }
</style>
</head>
<body>
  <h2>Merhaba, Laboratuvar!</h2>
  <p>Bu projeye "+ Dosya" ile yeni dosyalar ekleyebilir, zip'ten içe aktarabilirsin.</p>
  <button onclick="dene()">Köprüyü Dene</button>
  <p id="sonuc"></p>
  <script>
    function dene() {
      window.lonca.postMessage("merhaba kabuk!");
    }
    window.lonca.onmessage = function(event) {
      document.getElementById("sonuc").innerText = event.data;
    };
  </script>
</body>
</html>
"""
