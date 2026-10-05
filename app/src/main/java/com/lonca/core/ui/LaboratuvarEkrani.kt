package com.lonca.core.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lonca.core.kumhavuzu.ORNEK_INDEX_HTML
import com.lonca.core.veri.Dosya
import com.lonca.core.veri.DosyaDao
import com.lonca.core.veri.Sayfa
import com.lonca.core.veri.SayfaDao
import com.lonca.core.zip.zipIceAktar
import kotlinx.coroutines.launch

/**
 * Faz 4 — Laboratuvar artık sadece bir giriş noktası: boş bir proje
 * oluştur (gerçek dosya ağacıyla, Proje Düzenleyici'ye geçer) ya da bir
 * zip içe aktar (eski Lonca gibi çok dosyalı projeler dahil).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratuvarEkrani(
    sayfaDao: SayfaDao,
    dosyaDao: DosyaDao,
    onGeri: () -> Unit,
    onProjeyeGit: (Long) -> Unit
) {
    var isimMetni by remember { mutableStateOf("") }
    var durumMetni by remember { mutableStateOf<String?>(null) }
    val kapsam = rememberCoroutineScope()
    val context = LocalContext.current

    val zipSeciciBaslat = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val ad = isimMetni.trim().ifEmpty { "İçe Aktarılan Proje" }
            durumMetni = "İçe aktarılıyor..."
            kapsam.launch {
                try {
                    val yeniId = zipIceAktar(context, uri, ad, sayfaDao, dosyaDao)
                    durumMetni = null
                    onProjeyeGit(yeniId)
                } catch (hata: Exception) {
                    durumMetni = "İçe aktarılamadı: ${hata.message}"
                }
            }
        }
    }

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
        Column(modifier = Modifier.fillMaxSize().padding(icPadding).padding(16.dp)) {
            Text(
                text = "Yeni, boş bir proje oluştur ya da bir zip içe aktar (birden fazla dosya ve resim içerebilir).",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = isimMetni,
                onValueChange = { isimMetni = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Proje adı") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val ad = isimMetni.trim()
                    if (ad.isNotEmpty()) {
                        kapsam.launch {
                            val yeniId = sayfaDao.ekle(Sayfa(isim = ad, girisDosyaYolu = "index.html"))
                            dosyaDao.ekle(
                                Dosya(
                                    sayfaId = yeniId,
                                    yol = "index.html",
                                    icerikTuru = "metin",
                                    icerikMetin = ORNEK_INDEX_HTML,
                                    boyut = ORNEK_INDEX_HTML.length.toLong()
                                )
                            )
                            onProjeyeGit(yeniId)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Boş Proje Oluştur")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { zipSeciciBaslat.launch("application/zip") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zip'ten İçe Aktar")
            }

            val suAnkiDurum = durumMetni
            if (suAnkiDurum != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(suAnkiDurum, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
