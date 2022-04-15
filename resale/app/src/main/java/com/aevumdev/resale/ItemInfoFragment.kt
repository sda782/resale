package com.aevumdev.resale

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aevumdev.resale.databinding.FragmentItemInfoBinding
import com.aevumdev.resale.models.ItemViewModel
import com.aevumdev.resale.models.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId

class ItemInfoFragment : Fragment() {

    private var _binding: FragmentItemInfoBinding? = null
    private val args: ItemInfoFragmentArgs by navArgs()
    private val itemViewModel: ItemViewModel by activityViewModels()
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
        binding.priceInfoText.text = item.price.toString()
        binding.sellerInfoText.text = item.seller
        val date = Date(item.date*1000L)
        val timeStr = date.toString()
        binding.dateInfoText.text = timeStr

        if (FirebaseAuth.getInstance().currentUser?.email.toString() == item.seller) {
            binding.itemInfoDeleteBtn.setOnClickListener {
                itemViewModel.removeItem(item.id)
                findNavController().navigate(R.id.action_itemInfoFragment_to_itemListFragment)
            }
        } else {
            binding.itemInfoDeleteBtn.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
