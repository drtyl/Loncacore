package com.lonca.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lonca.core.ui.theme.LoncaMetinSoluk
import com.lonca.core.veri.Sayfa
import com.lonca.core.veri.SayfaDao

/**
 * "Dünya" — Laboratuvar'da üretilip sisteme eklenen her sayfa burada,
 * gerçek bir kart olarak beliriyor. Boşsa eski yer tutucu mesaj kalıyor.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DunyaEkrani(sayfaDao: SayfaDao, onLaboratuvaraGit: () -> Unit, onSayfayaGit: (Long) -> Unit) {
    val sayfalar by sayfaDao.hepsiniGetir().collectAsState(initial = emptyList())

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
        if (sayfalar.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(icPadding).padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(icPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sayfalar) { sayfa: Sayfa ->
                    Card(onClick = { onSayfayaGit(sayfa.id) }) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("\u2728", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = sayfa.isim,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
