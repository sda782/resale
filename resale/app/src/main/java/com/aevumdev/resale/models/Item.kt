package com.aevumdev.resale.models

import java.io.Serializable

data class Item(val id: Number, val title: String, val description: String, val price:Number, val seller:String,val date: Number, val pictureUrl: String):
    Serializable {
    constructor(title: String,description: String,price: Number,seller: String,pictureUrl: String) : this(-1,title,description,price,seller,100000,pictureUrl)

    override fun toString(): String {
        return "$id $title $description $price $seller $date $pictureUrl"
    }
}
