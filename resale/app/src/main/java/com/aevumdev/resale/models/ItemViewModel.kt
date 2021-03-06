package com.aevumdev.resale.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aevumdev.resale.repositories.ItemsRepository

class ItemViewModel : ViewModel() {
    private val repository = ItemsRepository()
    private var pitemsLiveData: MutableLiveData<List<Item>> = repository.itemsLiveData
    val itemsLiveData: LiveData<List<Item>> = pitemsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getItems()
    }

    fun addItem(item: Item) {
        repository.addItem(item)
    }

    fun removeItem(id: Int) {
        repository.removeItem(id)
    }

    fun getMaxPrice():Int{
        var maxPrice = 0
        repository.itemsLiveData.value?.forEach {
            if (it.price > maxPrice) maxPrice = it.price
        }
        return maxPrice
    }

    fun filterPrice(minVal:Int, maxVal:Int){
        pitemsLiveData.value = pitemsLiveData.value?.filter { it.price in (minVal + 1) until maxVal }
    }

    fun sort(sortType: String) {
        when (sortType) {
            "id" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedByDescending { it.id }
            }
            "title" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.title.lowercase() }
            }
            "description" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.description.lowercase() }
            }
            "price" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedByDescending { it.price }
            }
            "date" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedByDescending { it.date }
            }
            else -> {
                pitemsLiveData = repository.itemsLiveData
            }
        }
    }
}