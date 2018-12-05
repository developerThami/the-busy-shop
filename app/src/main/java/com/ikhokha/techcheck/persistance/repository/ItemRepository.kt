package com.ikhokha.techcheck.persistance.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import com.ikhokha.techcheck.persistance.AppDatabase
import com.ikhokha.techcheck.persistance.entities.CartItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemRepository(var context: Context) {

    var mDao = AppDatabase.getAppDatabase(context!!).itemDao()
    val cartItems: LiveData<List<CartItem>> get() =  mDao.allItemsInCart

    fun insertItem(cartItem: CartItem) {
        GlobalScope.launch {
            mDao.insert(cartItem)
        }
    }

    fun getItem(id: Int): CartItem {
        var item = CartItem()
        GlobalScope.launch {
            async {
                item = mDao.getItem(id)
            }.await()
        }
        return item
    }

    fun deleteAll() {
        mDao.deleteAll()
    }
}
