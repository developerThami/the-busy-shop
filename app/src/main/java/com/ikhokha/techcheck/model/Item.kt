package com.ikhokha.techcheck.model


class Item {

    lateinit var description: String
    lateinit var image: String
    lateinit var imageByteArray: ByteArray

    var price: Double = 0.0

    constructor(description: String, image: String, price: Double) {
        this.description = description
        this.image = image
        this.price = price
    }

    constructor() {}
}
