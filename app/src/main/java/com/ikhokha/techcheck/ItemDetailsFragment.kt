package com.ikhokha.techcheck

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import com.ikhokha.techcheck.persistance.entities.CartItem


@SuppressLint("ValidFragment")
class ItemDetailsFragment constructor(var item: CartItem) : Fragment() {

    private lateinit var itemBitmap: Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_item_details, container, false)

        val price = view.findViewById<TextView>(R.id.item_price)
        val image = view.findViewById<ImageView>(R.id.item_image)

        price.text = "R ${item.price}"
        itemBitmap = BitmapFactory.decodeByteArray(item.image, 0, item.image!!.size)
        image.setImageBitmap(itemBitmap)

        return view
    }
}
