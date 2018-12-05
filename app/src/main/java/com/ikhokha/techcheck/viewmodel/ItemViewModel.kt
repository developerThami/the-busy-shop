package com.ikhokha.techcheck.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.ikhokha.techcheck.persistance.entities.CartItem
import com.ikhokha.techcheck.persistance.repository.ItemRepository

class ItemViewModel(application: Application) : AndroidViewModel(application) {

     val repository: ItemRepository = ItemRepository(application)
     val cartItems: LiveData<List<CartItem>>

    init {
        cartItems = repository.cartItems
    }

    fun addItemToCart(item: CartItem) {
        repository.insertItem(item)
    }

    fun getScript(id: Int): CartItem {
        return repository.getItem(id)
    }
}
