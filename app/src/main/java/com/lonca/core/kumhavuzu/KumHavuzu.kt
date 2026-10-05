package com.lonca.core.kumhavuzu

import android.content.Context
import android.webkit.WebView
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature

/**
 * Kum havuzu — Laboratuvar'da yazılan HTML/CSS/JS'in çalıştığı izole alan.
 *
 * Güvenlik ilkeleri (Mimari Vizyon Raporu, Bölüm 5 ile aynı):
 * - Bu WebView'a SADECE kullanıcının kendi yazdığı, yerel içerik yüklenir
 *   (loadDataWithBaseURL ile, taban adres YOK) — hiçbir zaman internetten
 *   bir sayfa açmaz, bu yüzden dışarıdan enjeksiyon riski yok.
 * - allowFileAccess ve allowContentAccess kapalı — cihazın dosyalarına
 *   dokunamaz.
 * - Native tarafla TEK bağlantısı, "lonca" adında dar kapsamlı bir mesaj
 *   köprüsü (WebMessageListener) — eski, riskli addJavascriptInterface
 *   yöntemi BİLEREK kullanılmıyor.
 * - Faz 2'de köprü sadece basit bir "aldım" yanıtı veriyor; gerçek
 *   yetenekler (dil desteği, izinli eylemler) sonraki fazlarda, hep bu
 *   aynı dar kapıdan eklenecek.
 */
fun kumHavuzuWebViewOlustur(context: Context): WebView {
    val webView = WebView(context)

    webView.settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = false
        allowFileAccess = false
        allowContentAccess = false
    }

    if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
        WebViewCompat.addWebMessageListener(
            webView,
            "lonca",
            setOf("*")
        ) { _, message, _, _, replyProxy ->
            val gelenMesaj = message.data ?: ""
            // Faz 2: sadece kanıt amaçlı basit bir yanıt. Gerçek istek
            // türleri (örn. "dil_uret") sonraki fazlarda buraya eklenecek.
            replyProxy.postMessage("Kabuk şunu aldı: $gelenMesaj")
        }
    }

    return webView
}

/**
 * Laboratuvar'da yazılmaya başlanacak örnek, çok yalın bir sayfa —
 * mesaj köprüsünü de gösteriyor.
 */
const val ORNEK_BASLANGIC_KODU = """<!DOCTYPE html>
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
  <p>Burayı değiştirip tekrar Çalıştır'a basabilirsin.</p>
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
