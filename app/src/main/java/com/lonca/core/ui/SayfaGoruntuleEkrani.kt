package com.lonca.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lonca.core.kumhavuzu.kumHavuzuWebViewOlustur
import com.lonca.core.kumhavuzu.sayfaIkiliKlasoru
import com.lonca.core.kumhavuzu.sayfayiWebVieweYukle
import com.lonca.core.veri.DosyaDao
import com.lonca.core.veri.Sayfa
import com.lonca.core.veri.SayfaDao
import kotlinx.coroutines.launch

/**
 * Bir projeyi "çalıştırır" — kaydedilmiş dosya ağacını, gerçek yollarıyla
 * (kum havuzunun sahte sunucusu üzerinden) gösterir. Üstteki çöp kutusu
 * projeyi (hem Room kayıtlarını hem cihazdaki ikili dosyalarını) siler.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SayfaGoruntuleEkrani(sayfaId: Long, sayfaDao: SayfaDao, dosyaDao: DosyaDao, onGeri: () -> Unit) {
    var sayfa by remember { mutableStateOf<Sayfa?>(null) }
    val kapsam = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(sayfaId) {
        sayfa = sayfaDao.idIleGetir(sayfaId)
    }

    val mevcutSayfa = sayfa

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mevcutSayfa?.isim ?: "") },
                navigationIcon = {
                    IconButton(onClick = onGeri) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val silinecek = mevcutSayfa
                        if (silinecek != null) {
                            kapsam.launch {
                                dosyaDao.sayfayaGoreSilHepsi(silinecek.id)
                                sayfaIkiliKlasoru(context, silinecek.id).deleteRecursively()
                                sayfaDao.sil(silinecek)
                                onGeri()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Sil")
                    }
                }
            )
        }
    ) { icPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(icPadding)) {
            if (mevcutSayfa != null) {
                AndroidView(
                    factory = { ctx ->
                        kumHavuzuWebViewOlustur(ctx, mevcutSayfa.agIzniVar).also { webView ->
                            sayfayiWebVieweYukle(webView, mevcutSayfa.id, mevcutSayfa.girisDosyaYolu, ctx, dosyaDao)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
