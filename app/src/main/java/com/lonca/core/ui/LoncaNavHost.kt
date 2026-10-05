package com.lonca.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Tüm ekranların rota adları — tek yerden yönetilsin diye.
 */
object LoncaRotalari {
    const val DUNYA = "dunya"
    const val LABORATUVAR = "laboratuvar"
}

@Composable
fun LoncaNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = LoncaRotalari.DUNYA) {
        composable(LoncaRotalari.DUNYA) {
            DunyaEkrani(
                onLaboratuvaraGit = { navController.navigate(LoncaRotalari.LABORATUVAR) }
            )
        }
        composable(LoncaRotalari.LABORATUVAR) {
            LaboratuvarEkrani(
                onGeri = { navController.popBackStack() }
            )
        }
    }
}
