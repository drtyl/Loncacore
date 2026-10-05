package com.lonca.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
 * "Dünya" — Faz 1'de bilerek tamamen boş. Burada bir gün Laboratuvar'da
 * yazılan her şey yaşayacak (sayfa/araç/oda/karakter — ne üretilirse).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DunyaEkrani(onLaboratuvaraGit: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lonca") },
                actions = {
                    IconButton(onClick = onLaboratuvaraGit) {
                        Icon(Icons.Filled.Build, contentDescription = "Laboratuvar")
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Henüz bir şey yok", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Laboratuvar'da yazdığın her şey burada yaşayacak. Başlamak için sağ üstteki simgeye dokun.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LoncaMetinSoluk,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
