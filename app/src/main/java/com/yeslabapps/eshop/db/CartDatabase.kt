package com.yeslabapps.eshop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeslabapps.eshop.model.Cart

@Database(entities = [Cart :: class], version = 1)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDao() : CartDao

    companion object{

        @Volatile
        private var INSTANCE : CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }

}