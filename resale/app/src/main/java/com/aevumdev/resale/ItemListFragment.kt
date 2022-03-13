package com.aevumdev.resale

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aevumdev.resale.databinding.FragmentItemListBinding
import com.aevumdev.resale.models.GenericAdapter
import com.aevumdev.resale.models.ItemViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!
    private val itemViewModel : ItemViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

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
                val action = ItemListFragmentDirections.actionItemListFragmentToItemInfoFragment(position)
                findNavController().navigate(action)
            }
            binding.itemListRv.layoutManager = LinearLayoutManager(activity)
            binding.itemListRv.adapter = gAdapter
        }
        itemViewModel.reload()
        if (auth.currentUser == null){
            loginDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginDialog(){
        val loginDialogBuilder = AlertDialog.Builder(view?.context!!)
        loginDialogBuilder.setTitle("Login with Email")
        loginDialogBuilder.setCancelable(false)

        val inputEmail = EditText(context)
        inputEmail.hint = "email"
        inputEmail.inputType = InputType.TYPE_CLASS_TEXT

        val inputPassword = EditText(context)
        inputPassword.hint = "password"
        inputPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        val dialogView = LinearLayout(context, )
        dialogView.orientation = LinearLayout.VERTICAL
        dialogView.addView(inputEmail)
        dialogView.addView(inputPassword)

        loginDialogBuilder.setView(dialogView)
        loginDialogBuilder.setPositiveButton("Login", null)
        val loginDialog : AlertDialog = loginDialogBuilder.create()
        loginDialog.show()
        val positiveButton : Button = loginDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val email = inputEmail.editableText.toString()
            val password = inputPassword.editableText.toString()
            if (email.isEmpty()){
                inputEmail.error = "Enter an email"
                return@setOnClickListener
            }
            if (password.isEmpty()){
                inputPassword.error = "Enter a password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    loginDialog.dismiss()
                }
            }
        }
    }
}