package com.lonca.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lonca.core.kumhavuzu.kumHavuzuWebViewOlustur
import com.lonca.core.kumhavuzu.sayfayiWebVieweYukle
import com.lonca.core.veri.Dosya
import com.lonca.core.veri.DosyaDao
import com.lonca.core.veri.SayfaDao
import kotlinx.coroutines.launch

/**
 * Bir projenin dosya listesi: "+ Dosya" ile yeni bir metin dosyası
 * ekleyip düzenlemeye başlarsın, bir dosyaya dokunup açarsın, sağ
 * üstteki oynat simgesiyle tüm projeyi (gerçek dosya yollarıyla, kum
 * havuzunda) çalıştırıp önizlersin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjeDuzenleEkrani(
    sayfaId: Long,
    sayfaDao: SayfaDao,
    dosyaDao: DosyaDao,
    onGeri: () -> Unit,
    onDosyayaGit: (Long) -> Unit
) {
    val dosyalar by dosyaDao.sayfayaGoreGetir(sayfaId).collectAsState(initial = emptyList())
    var yeniDosyaYolu by remember { mutableStateOf("") }
    var onizlemeAcik by remember { mutableStateOf(false) }
    var girisDosyaYolu by remember { mutableStateOf("index.html") }
    val kapsam = rememberCoroutineScope()

    LaunchedEffect(sayfaId) {
        sayfaDao.idIleGetir(sayfaId)?.let { girisDosyaYolu = it.girisDosyaYolu }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (onizlemeAcik) "Önizleme" else "Proje") },
                navigationIcon = {
                    IconButton(onClick = { if (onizlemeAcik) onizlemeAcik = false else onGeri() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    if (!onizlemeAcik) {
                        IconButton(onClick = { onizlemeAcik = true }) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Çalıştır")
                        }
                    }
                }
            )
        }
    ) { icPadding ->
        if (onizlemeAcik) {
            Box(modifier = Modifier.fillMaxSize().padding(icPadding)) {
                AndroidView(
                    factory = { ctx ->
                        kumHavuzuWebViewOlustur(ctx).also { webView ->
                            sayfayiWebVieweYukle(webView, sayfaId, girisDosyaYolu, ctx, dosyaDao)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(icPadding)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = yeniDosyaYolu,
                        onValueChange = { yeniDosyaYolu = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("yeni-dosya.js") },
                        singleLine = true
                    )
                    Button(onClick = {
                        val yol = yeniDosyaYolu.trim()
                        if (yol.isNotEmpty()) {
                            kapsam.launch {
                                val yeniDosyaId = dosyaDao.ekle(
                                    Dosya(sayfaId = sayfaId, yol = yol, icerikTuru = "metin", icerikMetin = "", boyut = 0)
                                )
                                yeniDosyaYolu = ""
                                onDosyayaGit(yeniDosyaId)
                            }
                        }
                    }) {
                        Text("+ Dosya")
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(dosyalar) { dosya: Dosya ->
                        ListItem(
                            headlineContent = { Text(dosya.yol) },
                            supportingContent = {
                                Text(if (dosya.icerikTuru == "metin") "metin" else "ikili · ${dosya.boyut} bayt")
                            },
                            modifier = Modifier.clickable {
                                if (dosya.icerikTuru == "metin") onDosyayaGit(dosya.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
