package com.aevumdev.resale.repositories

import androidx.lifecycle.MutableLiveData
import com.aevumdev.resale.models.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemsRepository {
    private val url = "https://anbo-restresale.azurewebsites.net/api/"
    private val itemStoreService : ItemStoreService
    val itemsLiveData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()
    init {
        val build:Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        itemStoreService = build.create(ItemStoreService::class.java)
        getItems()
    }

    fun getItems() {
        itemStoreService.getAllItems().enqueue(object :Callback<List<Item>>{
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful){
                    itemsLiveData.postValue(response.body())
                    errorMessageLiveData.postValue("")
                }else{
                    val msg = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(msg)
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }

        })
    }

    fun addItem(item: Item) {
        itemStoreService.addItem(item).enqueue(object : Callback<Item>{
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful){
                    updateMessageLiveData.postValue("added " + response.body())
                    errorMessageLiveData.postValue("")
                }else{
                    val msg = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(msg)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }

        })
    }

    fun removeItem(id: Int) {
        itemStoreService.deleteItem(id).enqueue(object : Callback<Item>{
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful){
                    updateMessageLiveData.postValue("deleted " + response.body())
                    errorMessageLiveData.postValue("")
                }else{
                    val msg = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(msg)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }

        })
    }
}