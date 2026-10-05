package com.lonca.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lonca.core.ui.theme.LoncaMetinSoluk

/**
 * Faz 1'de yer tutucu — gerçek kod editörü (HTML/CSS/JS, Çalıştır/Önizle/Ekle
 * akışı, WebView tabanlı kum havuzu) Faz 2'de buraya gelecek.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratuvarEkrani(onGeri: () -> Unit) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(icPadding)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Kod editörü (Faz 2) burada gelecek — yaz, çalıştır, önizle, Dünya'ya ekle.",
                color = LoncaMetinSoluk,
                textAlign = TextAlign.Center
            )
        }
    }
}
