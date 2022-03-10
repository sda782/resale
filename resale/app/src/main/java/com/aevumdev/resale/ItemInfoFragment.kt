package com.aevumdev.resale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aevumdev.resale.databinding.FragmentItemInfoBinding
import com.aevumdev.resale.models.ItemViewModel

class ItemInfoFragment : Fragment() {

    private var _binding: FragmentItemInfoBinding? = null
    private val args: ItemInfoFragmentArgs by navArgs()
    private val itemViewModel : ItemViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemInfoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = itemViewModel.itemsLiveData.value?.get(args.itemInfo)!!
        binding.nameInfoText.text = item.title
        binding.descriptionInfoText.text = item.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}