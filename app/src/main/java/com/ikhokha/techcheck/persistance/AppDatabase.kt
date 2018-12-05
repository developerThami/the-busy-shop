package com.ikhokha.techcheck.persistance

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ikhokha.techcheck.persistance.dao.ItemDao

import com.ikhokha.techcheck.persistance.entities.CartItem

@Database(entities = arrayOf(CartItem::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "cart-database")
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
