package com.ikhokha.techcheck.persistance.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.ikhokha.techcheck.persistance.entities.CartItem

@Dao
interface ItemDao {

    @get:Query("SELECT * from item ORDER BY date_created_milliseconds ASC")
    val allItemsInCart: LiveData<List<CartItem>>

    @Insert(onConflict = REPLACE)
    fun insert(script: CartItem)

    @Query("DELETE FROM item")
    fun deleteAll()

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): CartItem
}
