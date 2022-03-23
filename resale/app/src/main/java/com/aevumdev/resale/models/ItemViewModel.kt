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

    fun sort(sortType: String) {
        when (sortType) {
            "id" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.id }
            }
            "title" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.title.lowercase() }
            }
            "description" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.description.lowercase() }
            }
            "price" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.price }
            }
            "date" -> {
                pitemsLiveData.value = pitemsLiveData.value?.sortedBy { it.date }
            }
            else -> {
                pitemsLiveData = repository.itemsLiveData
            }
        }
    }
}