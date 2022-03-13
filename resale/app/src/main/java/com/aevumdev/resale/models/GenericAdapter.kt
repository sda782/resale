package com.aevumdev.resale.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aevumdev.resale.databinding.SingleListItemBinding

class GenericAdapter(
    private val list: List<Item>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, onItemClicked)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.itemNameTxt.text = this.title
                binding.itemPriceTxt.text = this.price.toString()
            }
        }
    }

    inner class ViewHolder(val binding: SingleListItemBinding, val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root),View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
       return list.count()
    }
}

