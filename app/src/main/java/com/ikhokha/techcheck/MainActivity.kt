package com.ikhokha.techcheck

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.title = "Cart"

        val fragment = CartItemListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .commit()
    }
}
