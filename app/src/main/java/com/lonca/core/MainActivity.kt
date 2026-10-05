package com.lonca.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lonca.core.ui.LoncaNavHost
import com.lonca.core.ui.theme.LoncaCoreTheme

/**
 * Tek Activity — gerisi Jetpack Compose navigasyonuyla (LoncaNavHost)
 * yönetiliyor. Faz 1: boş Dünya ekranı + Laboratuvar'a geçiş (yer tutucu).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoncaCoreTheme {
                LoncaNavHost()
            }
        }
    }
}
