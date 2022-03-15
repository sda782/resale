package com.aevumdev.resale.repositories

import com.aevumdev.resale.models.Item
import retrofit2.Call
import retrofit2.http.*

interface ItemStoreService {
    @GET("resaleitems")
    fun getAllItems(): Call<List<Item>>

    @POST("resaleitems")
    fun addItem(@Body item:Item): Call<Item>
}