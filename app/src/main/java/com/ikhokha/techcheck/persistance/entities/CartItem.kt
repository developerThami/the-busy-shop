package com.ikhokha.techcheck.persistance.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable


@Entity(tableName = "item")
class CartItem : Parcelable {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "image_url")
    var uri: String? = null

    @ColumnInfo(name = "price")
    var price: String? = null

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

    @ColumnInfo(name = "date_created_milliseconds")
    var dateInMilli: Long = 0

    constructor() {}

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        description = `in`.readString()
        uri = `in`.readString()
        price = `in`.readString()
        image = `in`.createByteArray()
        dateInMilli = `in`.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(description)
        dest.writeString(uri)
        dest.writeString(price)
        dest.writeByteArray(image)
        dest.writeLong(dateInMilli)
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
