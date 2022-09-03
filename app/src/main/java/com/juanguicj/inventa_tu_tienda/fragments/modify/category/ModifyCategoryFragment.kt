package com.juanguicj.inventa_tu_tienda.fragments.modify.category

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentModifyCategoryBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import com.juanguicj.inventa_tu_tienda.main.setSpinnerEntries
import kotlin.math.log

class ModifyCategoryFragment : Fragment() {
    private lateinit var binding: FragmentModifyCategoryBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var modifyCategoryViewModel: ModifyCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentModifyCategoryBinding.inflate(layoutInflater)
        modifyCategoryViewModel = ViewModelProvider(this)[ModifyCategoryViewModel::class.java]
        val myContext = this.requireContext()

        with(binding){
            with(modifyCategoryViewModel){
                modifyCategoriesOperateButton.setOnClickListener {
                    ADD_operation(modifyCategoriesNewCategoryEditText.text.toString())
                }

                errorLiveData.observe(this@ModifyCategoryFragment){
                        error->
                    val toast = Toast.makeText(requireContext().applicationContext, error, Toast.LENGTH_SHORT)
                    toast.show()
                }
                confirmationADDLiveData.observe(this@ModifyCategoryFragment){
                    val message = getString(R.string.modifyCategories__ADD__confirmation)
                    mainViewModel.setCategoryChange()
                    modifyCategoriesNewCategoryEditText.setText("")
                    val toast = Toast.makeText(requireContext().applicationContext, message, Toast.LENGTH_SHORT)
                    toast.show()
                }
                confirmationRENAMELiveData.observe(this@ModifyCategoryFragment){
                    val message = getString(R.string.modifyCategories__RENAME__confirmation)
                    mainViewModel.setCategoryChange()
                    modifyCategoriesNewCategoryEditText.setText("")
                    val toast = Toast.makeText(requireContext().applicationContext, message, Toast.LENGTH_SHORT)
                    toast.show()
                }
                confirmationERASELiveData.observe(this@ModifyCategoryFragment){
                    val builder: AlertDialog.Builder? = activity?.let {
                        AlertDialog.Builder(it)
                    }
                    val category = modifyCategoriesSelectedCategorySpinner.selectedItem.toString()
                    val message = getString(R.string.modifyCategories__ERASE__alert, category)
                    builder?.setMessage(message)
                        ?.setPositiveButton(R.string.modifyCategories__positiveOption__dialog){ _, _ ->
                            DELETE_operation(category)
                            mainViewModel.setCategoryChange()
                            val messageToast = getString(R.string.modifyCategories__ERASE__confirmation)
                            val toast = Toast.makeText(requireContext().applicationContext, messageToast, Toast.LENGTH_LONG)
                            toast.show()
                        }
                        ?.setNegativeButton(R.string.modifyCategories__negativeOption__dialog){ _,_-> }
                    builder?.create()?.show()
                }

                modifyCategoriesAddButton.setOnClickListener {
                    showRenameFields()
                    modifyCategoriesOperateButton.text = getString(R.string.modifyCategories__add__text)
                    modifyCategoriesOperateButton.setOnClickListener {
                        ADD_operation(modifyCategoriesNewCategoryEditText.text.toString())
                    }
                }
                modifyCategoriesDeleteButton.setOnClickListener {
                    hideRenameFields()
                    modifyCategoriesOperateButton.text = getString(R.string.modifyCategories__erase__text)
                    modifyCategoriesOperateButton.setOnClickListener {
                        checkFields_for_ERASE(modifyCategoriesSelectedCategorySpinner.selectedItem.toString())
                    }
                }
                modifyCategoriesRenameButton.setOnClickListener {
                    showRenameFields()
                    modifyCategoriesOperateButton.text = getString(R.string.modifyCategories__rename__text)
                    modifyCategoriesOperateButton.setOnClickListener {
                        val selectedCategory = modifyCategoriesSelectedCategorySpinner.selectedItem.toString()
                        val renameCategory = modifyCategoriesNewCategoryEditText.text.toString()
                        RENAME_Operation(selectedCategory, renameCategory)
                    }
                }

                if(myDictionary.isSessionActive()){
                    mainViewModel.linkFunctionGetUpdatedCategoriesToDataBase()
                }
                setSpinnerEntries(modifyCategoriesSelectedCategorySpinner, myContext, mainViewModel)
                mainViewModel.logInMainLiveData.observe(this@ModifyCategoryFragment){
                        logIn->
                    if(logIn){
                        mainViewModel.linkFunctionGetUpdatedCategoriesToDataBase()
                    }else{
                        mainViewModel.linkFunctionGetUpdatedCategoriesToDictionary()
                    }
                    setSpinnerEntries(modifyCategoriesSelectedCategorySpinner, myContext, mainViewModel)
                }
                mainViewModel.categoryChangeLiveData.observe(this@ModifyCategoryFragment){
                    setSpinnerEntries(modifyCategoriesSelectedCategorySpinner, myContext, mainViewModel)
                }
            }
        }
    }

    private fun FragmentModifyCategoryBinding.hideRenameFields() {
        modifyCategoriesNewCategoryEditText.visibility = View.INVISIBLE
        modifyCategoriesNewCategoryInfoTextView.visibility = View.INVISIBLE
    }
    private fun FragmentModifyCategoryBinding.showRenameFields() {
        modifyCategoriesNewCategoryEditText.visibility = View.VISIBLE
        modifyCategoriesNewCategoryInfoTextView.visibility = View.VISIBLE
    }
}