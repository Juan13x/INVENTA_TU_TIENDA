package com.juanguicj.inventa_tu_tienda.fragments.modify.products

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentModifyProductsBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import com.juanguicj.inventa_tu_tienda.main.setSpinnerEntries


class ModifyProductsFragment : Fragment() {
    private lateinit var binding: FragmentModifyProductsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var modifyProductsViewModel: ModifyProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentModifyProductsBinding.inflate(layoutInflater)
        modifyProductsViewModel = ViewModelProvider(this)[ModifyProductsViewModel::class.java]
        val myContext = this.requireContext()
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        with(binding){
            notVisibleWhenNotChange()
            modifyProductsQueryLinearLayout.visibility  = View.GONE

            modifyProductsOperateButton.setOnClickListener {
                defaultOperationForOperateButtonIsADD()
            }

            with(modifyProductsViewModel) {
                if (myDictionary.isSessionActive()) {
                    mainViewModel.linkFunctionGetUpdatedCategoriesToDataBase()
                }
                setSpinnerEntries(modifyProductsCategorySpinner, myContext, mainViewModel)

                errorLiveData.observe(this@ModifyProductsFragment) { error ->
                    val toast = Toast.makeText(
                        requireContext().applicationContext,
                        error,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }

                modifyProductsSeeButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenSee()
                    disableInformativeFields()
                    modifyProductsCodeEditText.setText("")
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__see__text)
                    buttonOperationWhenSee()
                }
                modifyProductsChangeButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenChange()
                    disableInformativeFields()
                    modifyProductsCodeEditText.setText("")
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__change__text)
                    buttonOperationWhenChange()
                }
                modifyProductsAddButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenAdd()
                    enableInformativeFields()
                    clearAllFields()
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__add__text)
                    buttonOperationWhenAdd()
                }
                modifyProductsDeleteButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenDelete()
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__delete__text)
                    buttonOperationWhenDelete()
                }

                mainViewModel.categoryChangeLiveData.observe(this@ModifyProductsFragment) {
                    setSpinnerEntries(modifyProductsCategorySpinner, myContext, mainViewModel)
                }
                mainViewModel.logInMainLiveData.observe(this@ModifyProductsFragment) { logIn ->
                    setSpinnerEntries(modifyProductsCategorySpinner, myContext, mainViewModel)
                }

                confirmationADDLiveData.observe(this@ModifyProductsFragment){
                    confirmationWhenAdd(builder)
                }
                confirmationUploadCHANGELiveData.observe(this@ModifyProductsFragment){
                    confirmationWhenUploadOnChange()
                }
                confirmationCHANGELiveData.observe(this@ModifyProductsFragment){
                    confirmationToAskToChange(builder)
                }
                confirmationSEELiveData.observe(this@ModifyProductsFragment){
                    confirmationWhenSee()
                }
                confirmationDELETELiveData.observe(this@ModifyProductsFragment){
                    confirmationToAskToDelete(builder)
                }
            }
        }
    }

    private fun FragmentModifyProductsBinding.confirmationWhenAdd(builder: AlertDialog.Builder?) {
        val name = modifyProductsNameEditText.text.toString()
        val code = modifyProductsCodeEditText.text.toString()
        val price = modifyProductsPriceEditText.text.toString()
        val amount = modifyProductsAmountEditText.text.toString()
        val category = modifyProductsCategorySpinner.selectedItem.toString()

        val message = getString(
            R.string.modifyProducts__ADD__confirmation,
            code,
            name,
            amount,
            price,
            category
        )
        builder?.setMessage(message)
            ?.setTitle(R.string.modifyProducts__add__text)
            ?.setPositiveButton(R.string.modifyProducts__positiveOption__dialog) { _, _ -> }
            ?.setNegativeButton(""){ _, _ -> }

        builder?.create()?.show()
        clearAllFields()
    }
    private fun FragmentModifyProductsBinding.defaultOperationForOperateButtonIsADD() {
        val code = modifyProductsCodeEditText.text.toString()
        val category = modifyProductsCategorySpinner.selectedItem.toString()
        val name = modifyProductsNameEditText.text.toString()
        val price = modifyProductsPriceEditText.text.toString()
        val amount = modifyProductsAmountEditText.text.toString()

        modifyProductsViewModel.addOperation(code, name, amount, price, category)
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenAdd() {
        modifyProductsOperateButton.setOnClickListener {
            defaultOperationForOperateButtonIsADD()
        }
    }

    private fun FragmentModifyProductsBinding.confirmationWhenSee() {
        with(modifyProductsViewModel){
            disableMainFields()
            val price = getAuxProduct().price
            val amount = getAuxProduct().amount
            val name = getAuxProduct().name
            modifyProductsPriceEditText.setText(price.toString())
            modifyProductsAmountEditText.setText(amount.toString())
            modifyProductsNameEditText.setText(name)
            visibilityWhenSeeAfterConfirmation()
        }
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenSee() {
        with(modifyProductsViewModel){
            modifyProductsUploadButton.setOnClickListener {
                val code = modifyProductsCodeEditText.text.toString()
                val category = modifyProductsCategorySpinner.selectedItem.toString()
                val categoryPosition = modifyProductsCategorySpinner.selectedItemPosition
                seeOperation(code, category, categoryPosition)
            }
            modifyProductsNewQueryButton.setOnClickListener {
                modifyProductsCodeEditText.setText("")
                hideInformativeFields()
                enableMainFields()
            }
        }
    }

    private fun FragmentModifyProductsBinding.confirmationToAskToChange(builder: AlertDialog.Builder?){
        with(modifyProductsViewModel){
            val currentCode = getAuxProduct().code
            val currentName = getAuxProduct().name
            val currentAmount = getAuxProduct().amount
            val currentPrice = getAuxProduct().price
            val currentCategory = getAuxProduct().categorySTR
            val newCode = getProductToChange().code
            val newName = getProductToChange().name
            val newAmount = getProductToChange().amount
            val newPrice = getProductToChange().price
            val newCategory = getProductToChange().categorySTR
            val message = getString(R.string.modifyProducts__CHANGE__confirmation,
                currentCode,
                currentName,
                currentAmount,
                currentPrice,
                currentCategory,
                newCode,
                newName,
                newAmount,
                newPrice,
                newCategory)
            builder?.setMessage(message)
                ?.setTitle(R.string.modifyProducts__change__text)
                ?.setPositiveButton(R.string.modifyProducts__positiveOption__dialog){ _, _ ->
                    confirmationWhenChange()
                }
                ?.setNegativeButton(R.string.modifyProducts__negativeOption__dialog){ _, _ -> }
            builder?.create()?.show()
        }
    }
    private fun FragmentModifyProductsBinding.confirmationWhenUploadOnChange(){
        with(modifyProductsViewModel){
            disableMainFields()
            modifyProductsNewCategorySpinner.adapter = modifyProductsCategorySpinner.adapter
            val code = getAuxProduct().code
            val category = getAuxProduct().category
            val price = getAuxProduct().price
            val amount = getAuxProduct().amount
            val name = getAuxProduct().name
            modifyProductsPriceEditText.setText(price)
            modifyProductsAmountEditText.setText(amount)
            modifyProductsNameEditText.setText(name)
            visibilityWhenChangeAfterUpload()
            modifyProductsOldCodeTextView.text = code
            modifyProductsNewCategorySpinner.setSelection(category)
            modifyProductsNewCodeEditText.setText(code)
            modifyProductsNewNameEditText.setText(name)
            modifyProductsNewPriceEditText.setText(price)
            modifyProductsNewAmountEditText.setText(amount)
        }
    }
    private fun FragmentModifyProductsBinding.confirmationWhenChange(){
        with(modifyProductsViewModel){
            changeOperation()
            modifyProductsCodeEditText.setText("")
            hideInformativeAndChangeableFields()
            enableMainFields()
        }
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenChange() {
        with(modifyProductsViewModel){
            modifyProductsOperateButton.setOnClickListener {
                val newCode = modifyProductsNewCodeEditText.text.toString()
                val newName = modifyProductsNewNameEditText.text.toString()
                val newAmount = modifyProductsNewAmountEditText.text.toString()
                val newPrice = modifyProductsNewPriceEditText.text.toString()
                val newCategory = modifyProductsNewCategorySpinner.selectedItem.toString()
                val newCategoryPosition = modifyProductsNewCategorySpinner.selectedItemPosition
                checkFieldsForChange(newCode, newName, newAmount, newPrice, newCategory, newCategoryPosition)
            }

            modifyProductsUploadButton.setOnClickListener {
                val code = modifyProductsCodeEditText.text.toString()
                val category = modifyProductsCategorySpinner.selectedItem.toString()
                val categoryPosition = modifyProductsCategorySpinner.selectedItemPosition
                uploadForChange(code, category, categoryPosition)
            }
            modifyProductsNewQueryButton.setOnClickListener {
                modifyProductsCodeEditText.setText("")
                hideInformativeAndChangeableFields()
                enableMainFields()
            }
        }
    }

    private fun FragmentModifyProductsBinding.confirmationToAskToDelete(builder: AlertDialog.Builder?){
        with(modifyProductsViewModel){
            val code = getAuxProduct().code
            val name = getAuxProduct().name
            val amount = getAuxProduct().amount
            val price = getAuxProduct().price
            val category = getAuxProduct().categorySTR
            val message = getString(R.string.modifyProducts__DELETE__confirmation,
                code, name,  amount, price, category)
            builder?.setMessage(message)
                ?.setTitle(R.string.modifyProducts__change__text)
                ?.setPositiveButton(R.string.modifyProducts__positiveOption__dialog){ _, _ ->
                    confirmationWhenDelete()
                }
                ?.setNegativeButton(R.string.modifyProducts__negativeOption__dialog){ _, _ -> }
            builder?.create()?.show()
        }
    }
    private fun FragmentModifyProductsBinding.confirmationWhenDelete(){
        with(modifyProductsViewModel){
            deleteOperation()
            modifyProductsCodeEditText.setText("")
        }
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenDelete() {
        with(modifyProductsViewModel){
            modifyProductsOperateButton.setOnClickListener {
                val code = modifyProductsCodeEditText.text.toString()
                val category = modifyProductsCategorySpinner.selectedItem.toString()
                val categoryPosition = modifyProductsCategorySpinner.selectedItemPosition
                checkFields(code, category, categoryPosition, 2)
            }
        }
    }

    private fun FragmentModifyProductsBinding.disableMainFields(){
        modifyProductsCodeEditText.isEnabled = false
        modifyProductsCategorySpinner.isEnabled = false
    }
    private fun FragmentModifyProductsBinding.enableMainFields(){
        modifyProductsCodeEditText.isEnabled = true
        modifyProductsCategorySpinner.isEnabled = true
    }
    private fun FragmentModifyProductsBinding.enableInformativeFields(){
        modifyProductsAmountEditText.isEnabled = true
        modifyProductsPriceEditText.isEnabled = true
        modifyProductsNameEditText.isEnabled = true
    }
    private fun FragmentModifyProductsBinding.disableInformativeFields(){
        modifyProductsAmountEditText.isEnabled = false
        modifyProductsPriceEditText.isEnabled = false
        modifyProductsNameEditText.isEnabled = false
    }
    private fun FragmentModifyProductsBinding.hideInformativeAndChangeableFields(){
        visibilityWhenChange()
    }
    private fun FragmentModifyProductsBinding.hideInformativeFields(){
        visibilityWhenSee()
    }

    private fun FragmentModifyProductsBinding.visibilityWhenChange(){
        visibilityWhenSee()
    }
    private fun FragmentModifyProductsBinding.visibilityWhenChangeAfterUpload() {
        visibilityWhenSeeAfterConfirmation()
        modifyProductsOldCodeTextView.visibility = View.VISIBLE
        modifyProductsOldCodeInfoTextView.visibility = View.VISIBLE
        modifyProductsNewCodeEditText.visibility = View.VISIBLE
        modifyProductsNewCodeInfoTextView.visibility = View.VISIBLE
        modifyProductsNewNameEditText.visibility = View.VISIBLE
        modifyProductsNewNameInfoTextView.visibility = View.VISIBLE
        modifyProductsNewPriceEditText.visibility = View.VISIBLE
        modifyProductsNewPriceInfoTextView.visibility = View.VISIBLE
        modifyProductsNewAmountEditText.visibility = View.VISIBLE
        modifyProductsNewAmountInfoTextView.visibility = View.VISIBLE
        modifyProductsNewCategoryInfoTextView.visibility = View.VISIBLE
        modifyProductsNewCategorySpinner.visibility = View.VISIBLE
        modifyProductsOperateButton.visibility = View.VISIBLE
    }
    private fun FragmentModifyProductsBinding.notVisibleWhenNotChange() {
        modifyProductsNewCategoryInfoTextView.visibility = View.GONE
        modifyProductsNewCategorySpinner.visibility = View.GONE
        modifyProductsOldCodeTextView.visibility = View.GONE
        modifyProductsOldCodeInfoTextView.visibility = View.GONE
        modifyProductsNewCodeEditText.visibility = View.GONE
        modifyProductsNewCodeInfoTextView.visibility = View.GONE
        modifyProductsNewNameEditText.visibility = View.GONE
        modifyProductsNewNameInfoTextView.visibility = View.GONE
        modifyProductsNewPriceEditText.visibility = View.GONE
        modifyProductsNewPriceInfoTextView.visibility = View.GONE
        modifyProductsNewAmountEditText.visibility = View.GONE
        modifyProductsNewAmountInfoTextView.visibility = View.GONE
    }
    private fun FragmentModifyProductsBinding.visibilityWhenSee() {
        notVisibleWhenNotChange()
        modifyProductsOperateButton.visibility = View.INVISIBLE
        modifyProductsQueryLinearLayout.visibility  = View.VISIBLE
        modifyProductsPriceInfoTextView.visibility = View.GONE
        modifyProductsNameInfoTextView.visibility = View.GONE
        modifyProductsAmountInfoTextView.visibility = View.GONE
        modifyProductsAmountEditText.visibility = View.GONE
        modifyProductsPriceEditText.visibility = View.GONE
        modifyProductsNameEditText.visibility = View.GONE
    }
    private fun FragmentModifyProductsBinding.visibilityWhenSeeAfterConfirmation(){
        modifyProductsNameEditText.visibility = View.VISIBLE
        modifyProductsNameInfoTextView.visibility = View.VISIBLE
        modifyProductsPriceEditText.visibility = View.VISIBLE
        modifyProductsPriceInfoTextView.visibility = View.VISIBLE
        modifyProductsAmountEditText.visibility = View.VISIBLE
        modifyProductsAmountInfoTextView.visibility = View.VISIBLE
    }
    private fun FragmentModifyProductsBinding.visibilityWhenDelete() {
        notVisibleWhenNotChange()
        modifyProductsQueryLinearLayout.visibility  = View.GONE
        modifyProductsOperateButton.visibility = View.VISIBLE
        modifyProductsPriceInfoTextView.visibility = View.GONE
        modifyProductsNameInfoTextView.visibility = View.GONE
        modifyProductsAmountInfoTextView.visibility = View.GONE
        modifyProductsAmountEditText.visibility = View.GONE
        modifyProductsPriceEditText.visibility = View.GONE
        modifyProductsNameEditText.visibility = View.GONE
    }
    private fun FragmentModifyProductsBinding.visibilityWhenAdd() {
        notVisibleWhenNotChange()
        modifyProductsQueryLinearLayout.visibility  = View.GONE
        modifyProductsOperateButton.visibility = View.VISIBLE
        modifyProductsPriceInfoTextView.visibility = View.VISIBLE
        modifyProductsNameInfoTextView.visibility = View.VISIBLE
        modifyProductsAmountInfoTextView.visibility = View.VISIBLE
        modifyProductsAmountEditText.visibility = View.VISIBLE
        modifyProductsPriceEditText.visibility = View.VISIBLE
        modifyProductsNameEditText.visibility = View.VISIBLE
    }

    private fun FragmentModifyProductsBinding.clearAllFields(){
        modifyProductsCodeEditText.setText("")
        modifyProductsNameEditText.setText("")
        modifyProductsPriceEditText.setText("")
        modifyProductsAmountEditText.setText("")
    }
}