package com.aevumdev.resale

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aevumdev.resale.databinding.FragmentAddItemBinding
import com.aevumdev.resale.models.Item
import com.aevumdev.resale.models.ItemViewModel
import com.aevumdev.resale.models.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class AddItemFragment : Fragment() {
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private val itemViewModel : ItemViewModel by activityViewModels()
    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addItemBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addItemAddBtn.setOnClickListener {
            val titleText = binding.addItemTitleInput.text
            if (titleText.isEmpty()) {binding.addItemTitleInput.error = "Enter Title"; return@setOnClickListener}
            val descriptionText = binding.addItemDescriptionInput.text
            if (descriptionText.isEmpty()) {binding.addItemDescriptionInput.error = "Enter description"; return@setOnClickListener}
            val priceText = binding.addItemPriceInput.text
            if (priceText.isEmpty()) {binding.addItemPriceInput.error = "Enter Price"; return@setOnClickListener}
            val priceInt = priceText.toString().toInt()
            val addItem = Item(titleText.trim().toString(),descriptionText.trim().toString(),priceInt, FirebaseAuth.getInstance().currentUser?.email.toString())
            itemViewModel.addItem(addItem)
            Log.d("REX", addItem.toString())
            findNavController().navigate(R.id.action_addItemFragment_to_itemListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}