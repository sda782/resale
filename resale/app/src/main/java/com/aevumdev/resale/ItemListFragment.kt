package com.aevumdev.resale

import android.R.attr.password
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aevumdev.resale.databinding.FragmentItemListBinding
import com.aevumdev.resale.models.GenericAdapter
import com.aevumdev.resale.models.ItemViewModel
import com.aevumdev.resale.models.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!
    private var gridlayoutManager: GridLayoutManager? = null
    private val itemViewModel: ItemViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "All items"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewModel.itemsLiveData.observe(viewLifecycleOwner) { items ->
            if (items.count()>0) {
                binding.itemListRv.visibility = View.VISIBLE
                binding.itemListErrText.visibility = View.GONE
                val gAdapter = GenericAdapter(items) { position ->
                    val action =
                        ItemListFragmentDirections.actionItemListFragmentToItemInfoFragment(position)
                    findNavController().navigate(action)
                }

                gridlayoutManager = GridLayoutManager(this.context, 2)
                val currentOrientation = this.resources.configuration.orientation
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    gridlayoutManager?.spanCount = 4
                } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    gridlayoutManager?.spanCount = 2
                }

                binding.itemListRv.layoutManager = gridlayoutManager
                binding.itemListRv.adapter = gAdapter
            }else {
                binding.itemListRv.visibility = View.GONE
                binding.itemListErrText.visibility = View.VISIBLE
            }
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) { _ ->

            binding.fab.setOnClickListener {
                if (auth.currentUser != null) {
                    findNavController().navigate(R.id.action_itemListFragment_to_addItemFragment)
                } else {
                    loginDialog()
                }
            }
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                itemViewModel.sort(binding.spinner.selectedItem.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.filterBtn.setOnClickListener {
            filterDialog()
        }

        binding.swiperefresh.setOnRefreshListener {
            itemViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginDialog() {
        val loginDialogBuilder = AlertDialog.Builder(view?.context!!)
        loginDialogBuilder.setTitle("Login with Email")
        loginDialogBuilder.setCancelable(false)

        val errText = TextView(context)
        errText.text = ""

        val inputEmail = EditText(context)
        inputEmail.hint = "email"
        inputEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val inputPassword = EditText(context)
        inputPassword.hint = "password"
        inputPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        inputPassword.transformationMethod = PasswordTransformationMethod()

        val dialogView = LinearLayout(context)
        dialogView.orientation = LinearLayout.VERTICAL
        dialogView.addView(errText)
        dialogView.addView(inputEmail)
        dialogView.addView(inputPassword)

        loginDialogBuilder.setView(dialogView)
        loginDialogBuilder.setPositiveButton("Login", null)
        loginDialogBuilder.setNeutralButton("Sign up", null)
        loginDialogBuilder.setNegativeButton("Back", null)
        val loginDialog: AlertDialog = loginDialogBuilder.create()
        loginDialog.show()

        val positiveButton: Button = loginDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val email = inputEmail.editableText.toString()
            val password = inputPassword.editableText.toString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.error = "Enter a valid email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                inputPassword.error = "Enter a password"
                return@setOnClickListener
            }
            userViewModel.signIn(view?.context!!, email, password)
            loginDialog.dismiss()
        }
        val neutralButton: Button = loginDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
        neutralButton.setOnClickListener {
            val email = inputEmail.editableText.toString()
            val password = inputPassword.editableText.toString()
            if (email.isEmpty()) {
                inputEmail.error = "Enter an email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                inputPassword.error = "Enter a password"
                return@setOnClickListener
            }
            userViewModel.signUp(view?.context!!, email, password)
            loginDialog.dismiss()
        }
        val negativeButton: Button = loginDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setOnClickListener {
            loginDialog.dismiss()
        }
    }

    private fun filterDialog() {
        val filterDialogBuilder = AlertDialog.Builder(view?.context!!)
        filterDialogBuilder.setTitle("Filter Price")
        val maxVal = itemViewModel.getMaxPrice()

        val minValText = TextView(context)
        val maxValText = TextView(context)

        minValText.text = "Min"
        maxValText.text = "Max"

        val minValSeekbar = SeekBar(context)
        val maxValSeekbar = SeekBar(context)

        minValSeekbar.max = maxVal
        maxValSeekbar.max = maxVal

        minValSeekbar.id = View.generateViewId()
        maxValSeekbar.id = View.generateViewId()

        minValSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                minValText.text = "Min " + minValSeekbar.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        maxValSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                maxValText.text = "Max " + maxValSeekbar.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        val dialogView = LinearLayout(context)
        dialogView.orientation = LinearLayout.VERTICAL

        dialogView.addView(minValText)
        dialogView.addView(minValSeekbar)
        dialogView.addView(maxValText)
        dialogView.addView(maxValSeekbar)

        filterDialogBuilder.setView(dialogView)

        filterDialogBuilder.setPositiveButton("Filter", null)
        filterDialogBuilder.setNegativeButton("Cancel", null)
        filterDialogBuilder.setNeutralButton("Reset", null)

        val filterDialog: AlertDialog = filterDialogBuilder.create()
        filterDialog.show()

        val positiveButton: Button = filterDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            itemViewModel.filterPrice(minValSeekbar.progress, maxValSeekbar.progress)
            filterDialog.dismiss()
        }
        val neutralButton: Button = filterDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
        neutralButton.setOnClickListener {
            itemViewModel.reload()
            filterDialog.dismiss()
        }
        val negativeButton: Button = filterDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setOnClickListener {
            filterDialog.dismiss()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridlayoutManager?.spanCount = 4
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridlayoutManager?.spanCount = 2
        }
        super.onConfigurationChanged(newConfig)
    }

}

