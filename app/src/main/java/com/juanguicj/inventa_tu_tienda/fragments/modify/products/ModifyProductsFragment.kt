package com.juanguicj.inventa_tu_tienda.fragments.modify.products

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentModifyProductsBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import android.R as R1


class ModifyProductsFragment : Fragment() {
    private lateinit var binding: FragmentModifyProductsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var modifyProductsViewModel: ModifyProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModifyProductsBinding.inflate(inflater, container, false)
        modifyProductsViewModel = ViewModelProvider(this)[ModifyProductsViewModel::class.java]
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        with(binding){
            notVisibleWhenNotChange()
            modifyProductsQueryLinearLayout.visibility  = View.GONE

            with(modifyProductsViewModel) {
                if (myDictionary.isSessionActive()) {
                    modifyProductsViewModel.linkFunctionGetUpdatedCategoriesToDataBase()
                }
                setSpinnerEntries(modifyProductsCategorySpinner)

                errorLiveData.observe(viewLifecycleOwner) { error ->
                    val toast = Toast.makeText(
                        requireContext().applicationContext,
                        error,
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }

                modifyProductsSeeButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenSee()
                    disableInformativeFields()
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__see__text)
                    buttonOperationWhenSee()
                    confirmationWhenSee()
                }
                modifyProductsChangeButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenChange()
                    disableInformativeFields()
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__change__text)
                    buttonOperationWhenChange()
                    confirmationWhenUploadOnChange()
                    confirmationToAskToChange()
                    confirmationWhenChange()
                }
                modifyProductsAddButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenAdd()
                    enableInformativeFields()
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__add__text)
                    buttonOperationWhenAdd()
                    confirmationWhenAdd(builder)
                }
                modifyProductsDeleteButton.setOnClickListener {
                    enableMainFields()
                    visibilityWhenDelete()
                    modifyProductsOperateButton.text = getString(R.string.modifyProducts__operate__delete__text)
                }

                mainViewModel.categoryChangeLiveData.observe(viewLifecycleOwner) {
                    setSpinnerEntries(modifyProductsCategorySpinner)
                }
                mainViewModel.logInMainLiveData.observe(viewLifecycleOwner) { logIn ->
                    if (logIn) {
                        modifyProductsViewModel.linkFunctionGetUpdatedCategoriesToDataBase()
                    } else {
                        modifyProductsViewModel.linkFunctionGetUpdatedCategoriesToDictionary()
                    }
                    setSpinnerEntries(modifyProductsCategorySpinner)
                }
            }
        }

        return binding.root
    }

    private fun FragmentModifyProductsBinding.confirmationWhenAdd(builder: AlertDialog.Builder?) {
        fun FragmentModifyProductsBinding.confirmationOperationADD(
            builder: AlertDialog.Builder?
        ) {
            val name = modifyProductsNameEditText.text.toString()
            val code = modifyProductsCodeEditText.text.toString()
            val price = modifyProductsPriceEditText.text.toString()
            val amount = modifyProductsAmountEditText.text.toString()
            val category = modifyProductsCategorySpinner.selectedItem.toString()

            val message = getString(
                R.string.modifyProducts__ADD__confirmation,
                code,
                name,
                amount.toInt(),
                price.toFloat(),
                category
            )
            builder?.setMessage(message)
                ?.setTitle(R.string.modifyProducts__add__text)
                ?.setPositiveButton(R.string.modifyProducts__positiveOption__dialog)
                { _, _ -> }
            val dialog: AlertDialog? = builder?.create()
            dialog?.show()
        }
        with(modifyProductsViewModel){
            confirmationLiveData.removeObservers(viewLifecycleOwner)
            confirmationLiveData.observe(viewLifecycleOwner) {
                confirmationOperationADD(builder)
                val dialog: AlertDialog? = builder?.create()
                dialog?.show()
                clearAllFields()
            }
        }
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenAdd() {
        modifyProductsOperateButton.setOnClickListener {
            val code = modifyProductsCodeEditText.text.toString()
            val category = modifyProductsCategorySpinner.selectedItem.toString()
            val name = modifyProductsNameEditText.text.toString()
            val price = modifyProductsPriceEditText.text.toString()
            val amount = modifyProductsAmountEditText.text.toString()

            modifyProductsViewModel.addOperation(code, name, amount, price, category)
        }
    }

    private fun FragmentModifyProductsBinding.confirmationWhenSee() {
        with(modifyProductsViewModel){
            confirmationLiveData.removeObservers(viewLifecycleOwner)
            confirmationLiveData.observe(viewLifecycleOwner){
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
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenSee() {
        with(modifyProductsViewModel){
            modifyProductsUploadButton.setOnClickListener {
                    val code = modifyProductsCodeEditText.text.toString()
                    val category = modifyProductsCategorySpinner.selectedItem.toString()
                    seeOperation(code, category)
            }
            modifyProductsNewQueryButton.setOnClickListener {
                hideInformativeFields()
                enableMainFields()
            }
            }
        }

    private fun FragmentModifyProductsBinding.confirmationToAskToChange(){

    }
    private fun FragmentModifyProductsBinding.confirmationWhenUploadOnChange(){
        with(modifyProductsViewModel){
            confirmationUploadChangeLiveData.removeObservers(viewLifecycleOwner)
            confirmationUploadChangeLiveData.observe(viewLifecycleOwner){
                disableMainFields()
                setSpinnerEntries(modifyProductsNewCategorySpinner)
                val code = getAuxProduct().code
                val category = getAuxProduct().category
                val price = getAuxProduct().price
                val amount = getAuxProduct().amount
                val name = getAuxProduct().name
                modifyProductsPriceEditText.setText(price.toString())
                modifyProductsAmountEditText.setText(amount.toString())
                modifyProductsNameEditText.setText(name)
                visibilityWhenChangeAfterUpload()
                modifyProductsOldCodeTextView.text = code
                modifyProductsNewCategorySpinner.setSelection(category)
                modifyProductsNewCodeEditText.setText(code)
                modifyProductsNewNameEditText.setText(name)
                modifyProductsNewPriceEditText.setText(price.toString())
                modifyProductsNewAmountEditText.setText(amount.toString())
            }
        }
    }
    private fun FragmentModifyProductsBinding.confirmationWhenChange() {
        with(modifyProductsViewModel){
            confirmationLiveData.removeObservers(viewLifecycleOwner)
            confirmationLiveData.observe(viewLifecycleOwner){
            }
        }
    }
    private fun FragmentModifyProductsBinding.buttonOperationWhenChange() {
        with(modifyProductsViewModel){
            modifyProductsOperateButton.setOnClickListener {

            }

            modifyProductsUploadButton.setOnClickListener {
                val code = modifyProductsCodeEditText.text.toString()
                val category = modifyProductsCategorySpinner.selectedItem.toString()
                uploadForChange(code, category)
            }
            modifyProductsNewQueryButton.setOnClickListener {
                hideInformativeAndChangeableFields()
                enableMainFields()
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

    private fun setSpinnerEntries(spinner: Spinner) {
        val changedSpinnerList: ArrayList<String> = modifyProductsViewModel.getUpdatedCategories()
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this@ModifyProductsFragment.requireContext(),
            R1.layout.simple_spinner_item,
            changedSpinnerList
        )
        adapter.setDropDownViewResource(R1.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}