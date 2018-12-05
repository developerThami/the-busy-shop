package com.ikhokha.techcheck

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ikhokha.techcheck.model.Item
import com.ikhokha.techcheck.persistance.entities.CartItem

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    lateinit var context: Context
    private  var itemList: List<CartItem> = emptyList()

    private var listener: OnItemSelectedListener? = null

    interface OnItemSelectedListener {
        fun itemSelected(tem: CartItem)
    }

    fun setItemList(itemList: List<CartItem>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun setListener( listener: OnItemSelectedListener) {
        this.listener = listener
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item, parent, false)
        val vh = ItemViewHolder(view)
        view.setOnClickListener { listener!!.itemSelected(itemList!![vh.adapterPosition]) }
        return vh
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList!![position])
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var itemImage: ImageView = view.findViewById(R.id.cart_item_image)
        var itemDescription: TextView = view.findViewById(R.id.cart_description)
        var itemPrice: TextView = view.findViewById(R.id.cart_item_price)

        fun bind(item: CartItem) {

            val byteArray = item.image
            val bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray!!.size)

            itemDescription.text = item.description
            itemPrice.text = item.price.toString()

            itemImage.setImageBitmap(bitmap)
        }
    }
}


