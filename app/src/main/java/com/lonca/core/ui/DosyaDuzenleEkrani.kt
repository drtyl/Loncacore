package com.lonca.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.lonca.core.veri.DosyaDao
import kotlinx.coroutines.launch

/** Tek bir metin dosyasını (html/css/js/...) düzenleme ekranı. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosyaDuzenleEkrani(dosyaId: Long, dosyaDao: DosyaDao, onGeri: () -> Unit) {
    var yol by remember { mutableStateOf("") }
    var icerik by remember { mutableStateOf("") }
    val kapsam = rememberCoroutineScope()

    LaunchedEffect(dosyaId) {
        dosyaDao.idIleGetir(dosyaId)?.let { dosya ->
            yol = dosya.yol
            icerik = dosya.icerikMetin ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(yol) },
                navigationIcon = {
                    IconButton(onClick = {
                        kapsam.launch {
                            dosyaDao.metinGuncelle(dosyaId, icerik, icerik.length.toLong())
                            onGeri()
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kaydet ve Geri")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        kapsam.launch {
                            dosyaDao.metinGuncelle(dosyaId, icerik, icerik.length.toLong())
                        }
                    }) {
                        Icon(Icons.Filled.Check, contentDescription = "Kaydet")
                    }
                    IconButton(onClick = {
                        kapsam.launch {
                            dosyaDao.idIleGetir(dosyaId)?.let { dosyaDao.sil(it) }
                            onGeri()
                        }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Sil")
                    }
                }
            )
        }
    ) { icPadding ->
        OutlinedTextField(
            value = icerik,
            onValueChange = { icerik = it },
            modifier = Modifier.fillMaxSize().padding(icPadding).padding(12.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        )
    }
}
