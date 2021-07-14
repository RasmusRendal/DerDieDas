package com.machiav3lli.derdiedas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.machiav3lli.derdiedas.NOUN_DB_NAME

@Database(entities = [Noun::class], version = 1)
abstract class NounDatabase : RoomDatabase() {
    abstract val nounDao: NounDao

    companion object {
        @Volatile
        private var INSTANCE: NounDatabase? = null

        fun getInstance(context: Context): NounDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context.applicationContext,
                            NounDatabase::class.java,
                            NOUN_DB_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}