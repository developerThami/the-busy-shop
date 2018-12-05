package com.ikhokha.techcheck


import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.storage.FirebaseStorage
import com.ikhokha.techcheck.model.Item
import com.ikhokha.techcheck.persistance.entities.CartItem
import com.ikhokha.techcheck.persistance.repository.ItemRepository
import com.ikhokha.techcheck.viewmodel.ItemViewModel
import java.util.*

class CartItemListFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var item: Item

    lateinit var cartList: RecyclerView
    lateinit var addNewItem: FloatingActionButton

    private lateinit var viewModel: ItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart_item_list, container, false)

        cartList = view.findViewById(R.id.cart_list)
        addNewItem = view.findViewById(R.id.add_new)

        addNewItem.setOnClickListener {
            takeBarcodeImage()
        }

        viewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java!!)
        val adapter = ItemAdapter()

        viewModel.cartItems.observeForever { items ->

            adapter.setItemList(items!!)
            adapter.setListener(object : ItemAdapter.OnItemSelectedListener{
                override fun itemSelected(tem: CartItem) {

                }
            })
            cartList.layoutManager = LinearLayoutManager(context)
            cartList.setHasFixedSize(true)
            cartList.adapter = adapter

        }

        return view
    }

    private fun takeBarcodeImage() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            var bitmap = data!!.extras.get("data") as Bitmap
            val barcodeImageBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            scanBarcodeInformation(barcodeImageBitmap)
        }
    }

    private fun scanBarcodeInformation(barcodeImageBitmap: Bitmap) {

        val b = BitmapFactory.decodeResource(context!!.resources, R.drawable.barcode)
        val visionImage = FirebaseVisionImage.fromBitmap(barcodeImageBitmap)

        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(visionImage).addOnSuccessListener {

            for (barcode in it) {
                val barcodeNumber = barcode.rawValue
                getItemInformation(barcodeNumber!!)
            }

            if (it.size == 0){

                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Scan Failed!")
                builder.setMessage("Failed to scan item please re-scan barcode.")
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.setPositiveButton("Try Again") { _, _ ->
                    takeBarcodeImage()
                }

                builder.create().show()

            }

        }.addOnFailureListener {
            Log.d("----error", it.localizedMessage)
        }
    }

    private fun getItemInformation(path: String) {

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference(path)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                item = dataSnapshot.getValue<Item>(Item::class.java!!)!!
                getItemImage(item)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    private fun getItemImage(item: Item) {

        val url = "gs://the-busy-shop.appspot.com"

        val storage = FirebaseStorage.getInstance(url)
        val storageRef = storage.reference

        storageRef.child(item.image).getBytes(Long.MAX_VALUE).addOnSuccessListener {
            item.imageByteArray = it
            saveItem(item)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    private fun saveItem(item: Item) {
        val cartItem = CartItem()
        cartItem.description = item.description
        cartItem.dateInMilli = Calendar.getInstance().timeInMillis
        cartItem.price = item.price.toString()
        cartItem.image = item.imageByteArray

        val repository = ItemRepository(context!!)
        repository.insertItem(cartItem)
    }
}
