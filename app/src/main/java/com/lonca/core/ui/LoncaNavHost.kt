package com.lonca.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lonca.core.veri.DosyaDao
import com.lonca.core.veri.SayfaDao

/**
 * Tüm ekranların rota adları — tek yerden yönetilsin diye.
 */
object LoncaRotalari {
    const val DUNYA = "dunya"
    const val LABORATUVAR = "laboratuvar"
    const val PROJE_DUZENLE = "proje/{sayfaId}"
    const val DOSYA_DUZENLE = "projeDosya/{dosyaId}"
    const val SAYFA_GORUNTULE = "sayfa/{sayfaId}"

    fun projeDuzenleRotasi(sayfaId: Long): String = "proje/$sayfaId"
    fun dosyaDuzenleRotasi(dosyaId: Long): String = "projeDosya/$dosyaId"
    fun sayfaGoruntuleRotasi(sayfaId: Long): String = "sayfa/$sayfaId"
}

@Composable
fun LoncaNavHost(
    sayfaDao: SayfaDao,
    dosyaDao: DosyaDao,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = LoncaRotalari.DUNYA) {
        composable(LoncaRotalari.DUNYA) {
            DunyaEkrani(
                sayfaDao = sayfaDao,
                onLaboratuvaraGit = { navController.navigate(LoncaRotalari.LABORATUVAR) },
                onSayfayaGit = { sayfaId -> navController.navigate(LoncaRotalari.sayfaGoruntuleRotasi(sayfaId)) }
            )
        }
        composable(LoncaRotalari.LABORATUVAR) {
            LaboratuvarEkrani(
                sayfaDao = sayfaDao,
                dosyaDao = dosyaDao,
                onGeri = { navController.popBackStack() },
                onProjeyeGit = { sayfaId ->
                    navController.navigate(LoncaRotalari.projeDuzenleRotasi(sayfaId)) {
                        popUpTo(LoncaRotalari.DUNYA)
                    }
                }
            )
        }
        composable(
            route = LoncaRotalari.PROJE_DUZENLE,
            arguments = listOf(navArgument("sayfaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sayfaId = backStackEntry.arguments?.getLong("sayfaId") ?: 0L
            ProjeDuzenleEkrani(
                sayfaId = sayfaId,
                sayfaDao = sayfaDao,
                dosyaDao = dosyaDao,
                onGeri = { navController.popBackStack() },
                onDosyayaGit = { dosyaId -> navController.navigate(LoncaRotalari.dosyaDuzenleRotasi(dosyaId)) }
            )
        }
        composable(
            route = LoncaRotalari.DOSYA_DUZENLE,
            arguments = listOf(navArgument("dosyaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val dosyaId = backStackEntry.arguments?.getLong("dosyaId") ?: 0L
            DosyaDuzenleEkrani(
                dosyaId = dosyaId,
                dosyaDao = dosyaDao,
                onGeri = { navController.popBackStack() }
            )
        }
        composable(
            route = LoncaRotalari.SAYFA_GORUNTULE,
            arguments = listOf(navArgument("sayfaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sayfaId = backStackEntry.arguments?.getLong("sayfaId") ?: 0L
            SayfaGoruntuleEkrani(
                sayfaId = sayfaId,
                sayfaDao = sayfaDao,
                dosyaDao = dosyaDao,
                onGeri = { navController.popBackStack() }
            )
        }
    }
}
