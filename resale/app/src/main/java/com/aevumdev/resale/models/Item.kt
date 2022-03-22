package com.aevumdev.resale.models

import java.io.Serializable

data class Item(val id: Int, val title: String, val description: String, val price:Int, val seller:String,val date: Int, val pictureUrl: String):
    Serializable {
    constructor(title: String,description: String,price: Int, seller: String) : this(-1,title,description,price,seller, (System.currentTimeMillis()/1000).toInt(),"")

    override fun toString(): String {
        return "$id $title $description $price $seller $date $pictureUrl"
    }
}
