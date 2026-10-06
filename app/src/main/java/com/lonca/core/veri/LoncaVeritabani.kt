package com.lonca.core.veri

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Faz 4: gerçek dosya/klasör sistemi eklendi (Dosya tablosu).
 * Faz 5: izin sistemi (Sayfa.agIzniVar) eklendi. Henüz erken geliştirme
 * aşamasında olduğumuz için (gerçek kullanıcı verisi riske girmiyor)
 * elle bir göç yazmak yerine fallbackToDestructiveMigration kullanıyoruz
 * — bu güncellemeyle daha önce kaydettiğin sayfalar silinecek.
 */
@Database(entities = [Sayfa::class, Dosya::class], version = 4, exportSchema = false)
abstract class LoncaVeritabani : RoomDatabase() {
    abstract fun sayfaDao(): SayfaDao
    abstract fun dosyaDao(): DosyaDao

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
                )
                    .fallbackToDestructiveMigration()
                    .build()
                ORNEK = yeniOrnek
                return yeniOrnek
            }
        }
    }
}
