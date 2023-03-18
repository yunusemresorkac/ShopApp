package com.yeslabapps.eshop.db

import android.content.Context
import android.speech.tts.Voice
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeslabapps.eshop.model.Cart

@Database(entities = [Cart :: class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao() : CartDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }

}