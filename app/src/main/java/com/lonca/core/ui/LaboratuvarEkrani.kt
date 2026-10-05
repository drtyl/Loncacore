package com.lonca.core.ui

import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.lonca.core.kumhavuzu.ORNEK_BASLANGIC_KODU
import com.lonca.core.kumhavuzu.kumHavuzuWebViewOlustur
import com.lonca.core.ui.theme.LoncaMetinSoluk
import com.lonca.core.veri.Sayfa
import com.lonca.core.veri.SayfaDao
import kotlinx.coroutines.launch

/**
 * Faz 3 — kod editörü artık kalıcı: bir ad ver, "Sisteme Ekle" ile
 * Dünya'ya gerçekten eklenir (Room veritabanına yazılır).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratuvarEkrani(sayfaDao: SayfaDao, onGeri: () -> Unit) {
    var isimMetni by remember { mutableStateOf("") }
    var kodMetni by remember { mutableStateOf(ORNEK_BASLANGIC_KODU) }
    var webViewReferansi by remember { mutableStateOf<WebView?>(null) }
    val kapsam = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laboratuvar") },
                navigationIcon = {
                    IconButton(onClick = onGeri) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { icPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(icPadding)) {

            OutlinedTextField(
                value = isimMetni,
                onValueChange = { isimMetni = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                label = { Text("Sayfa adı") },
                singleLine = true
            )

            OutlinedTextField(
                value = kodMetni,
                onValueChange = { kodMetni = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.36f)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                label = { Text("HTML / CSS / JS") },
                textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = false)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        webViewReferansi?.loadDataWithBaseURL(null, kodMetni, "text/html", "UTF-8", null)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Text("  Çalıştır")
                }

                Button(
                    onClick = {
                        val ad = isimMetni.trim()
                        if (ad.isNotEmpty()) {
                            kapsam.launch {
                                sayfaDao.ekle(Sayfa(isim = ad, icerik = kodMetni))
                                onGeri()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sisteme Ekle")
                }
            }

            Text(
                text = "Önizleme (kum havuzu — izole çalışır)",
                style = MaterialTheme.typography.bodySmall,
                color = LoncaMetinSoluk,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.46f)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                AndroidView(
                    factory = { context ->
                        kumHavuzuWebViewOlustur(context).also { webView ->
                            webViewReferansi = webView
                            webView.loadDataWithBaseURL(null, kodMetni, "text/html", "UTF-8", null)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
