package com.aevumdev.resale.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aevumdev.resale.repositories.ItemsRepository

class ItemViewModel : ViewModel() {
    private val repository = ItemsRepository()
    val itemsLiveData : LiveData<List<Item>> = repository.itemsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    init {
        reload()
    }
    fun reload(){
        repository.getItems()
    }
    fun addItem(item:Item){
        repository.addItem(item)
    }

    fun removeItem(id: Int) {
        repository.removeItem(id)
    }
}