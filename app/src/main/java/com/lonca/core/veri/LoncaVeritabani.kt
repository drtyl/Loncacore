package com.lonca.core.veri

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sayfa::class], version = 1, exportSchema = false)
abstract class LoncaVeritabani : RoomDatabase() {
    abstract fun sayfaDao(): SayfaDao

    companion object {
        @Volatile
        private var ORNEK: LoncaVeritabani? = null

        fun ornekGetir(context: Context): LoncaVeritabani {
            val mevcut = ORNEK
            if (mevcut != null) return mevcut
            synchronized(this) {
                val yeniOrnek = Room.databaseBuilder(
                    context.applicationContext,
                    LoncaVeritabani::class.java,
                    "lonca-veritabani"
                ).build()
                ORNEK = yeniOrnek
                return yeniOrnek
            }
        }
    }
}
