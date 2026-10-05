package com.lonca.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lonca.core.ui.LoncaNavHost
import com.lonca.core.ui.theme.LoncaCoreTheme
import com.lonca.core.veri.LoncaVeritabani

/**
 * Tek Activity — gerisi Jetpack Compose navigasyonuyla (LoncaNavHost)
 * yönetiliyor. Faz 3: Laboratuvar artık kalıcı (Room) — buradan tek bir
 * veritabanı örneği oluşturup tüm ekranlara geçiriyoruz.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val veritabani = LoncaVeritabani.ornekGetir(applicationContext)

        setContent {
            LoncaCoreTheme {
                LoncaNavHost(sayfaDao = veritabani.sayfaDao())
            }
        }
    }
}
