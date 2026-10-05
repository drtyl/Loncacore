package com.lonca.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lonca.core.veri.SayfaDao

/**
 * Tüm ekranların rota adları — tek yerden yönetilsin diye.
 */
object LoncaRotalari {
    const val DUNYA = "dunya"
    const val LABORATUVAR = "laboratuvar"
    const val SAYFA_GORUNTULE = "sayfa/{sayfaId}"

    fun sayfaGoruntuleRotasi(sayfaId: Long): String = "sayfa/$sayfaId"
}

@Composable
fun LoncaNavHost(sayfaDao: SayfaDao, navController: NavHostController = rememberNavController()) {
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
                onGeri = { navController.popBackStack() }
            )
        }
    }
}
