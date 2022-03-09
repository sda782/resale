package com.aevumdev.resale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aevumdev.resale.databinding.FragmentItemListBinding
import com.aevumdev.resale.models.GenericAdapter
import com.aevumdev.resale.models.Item
import com.aevumdev.resale.models.ItemViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!
    private val itemViewModel : ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemViewModel.itemsLiveData.observe(viewLifecycleOwner){items ->
            val gAdapter = GenericAdapter(items){ position ->
                val snackText = items[position].toString()
                val snack = Snackbar.make(view,snackText, Snackbar.LENGTH_LONG)
                snack.show()
            }
            binding.itemListRv.layoutManager = LinearLayoutManager(activity)
            binding.itemListRv.adapter = gAdapter
        }
        itemViewModel.reload()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}