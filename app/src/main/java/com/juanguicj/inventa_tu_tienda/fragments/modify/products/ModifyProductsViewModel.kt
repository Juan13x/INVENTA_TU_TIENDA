package com.juanguicj.inventa_tu_tienda.fragments.modify.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.ProductsType
import com.juanguicj.inventa_tu_tienda.main.myDataBase
import com.juanguicj.inventa_tu_tienda.main.myDictionary

class ModifyProductsViewModel : ViewModel() {
    data class ProductInfo(var code: String,
                           var name: String,
                           var price: Float,
                           var amount: Int,
                           var category: Int)
    private var auxProduct = ProductInfo(name = "", code = "", price = 0.0f, amount = 0, category = 0)
    private var productForChange = ProductInfo(name = "", code = "", price = 0.0f, amount = 0, category = 0)

    fun getAuxProduct(): ProductInfo{
        return auxProduct
    }

    private val confirmationUploadChangeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationUploadChangeLiveData: LiveData<Boolean> = confirmationUploadChangeMutableLiveData
    private val confirmationToProceedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationToProceedChangeLiveData: LiveData<Boolean> = confirmationToProceedMutableLiveData
    private val confirmationMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationLiveData: LiveData<Boolean> = confirmationMutableLiveData
    private val errorMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val errorLiveData: LiveData<Int> = errorMutableLiveData

    fun addOperation(code: String, name: String, amount: String, price: String,
                               category: String){
        fun checkFieldAddOperation(code: String, name: String, amount: String, price: String):
                Boolean{
            if(code.isEmpty()){
                errorMutableLiveData.value = R.string.modifyProducts__emptyCode__error
            }
            else if(price.isEmpty()){
                errorMutableLiveData.value = R.string.modifyProducts__emptyPrice__error
            }
            else if(amount.isEmpty()){
                errorMutableLiveData.value = R.string.modifyProducts__emptyAmount__error
            }
            else if(name.isEmpty()){
                errorMutableLiveData.value = R.string.modifyProducts__emptyName__error
            }
            else{
                return true
            }
            return false
        }

        if(checkFieldAddOperation(code, name, amount, price)){
            if(myDictionary.isSessionActive()){
                val user = myDictionary.getUser()
                if(myDataBase.containsProduct(user, category, code) == 1){
                    errorMutableLiveData.value = R.string.modifyProducts__existingCode__error
                }else{
                    val product = ProductsType(name, amount.toInt(), price.toFloat())
                    myDataBase.setProduct(user, category, code, product)
                    confirmationMutableLiveData.value = true
                }
            }
            else{
                if(myDictionary.containsProduct(category, code) == 1){
                    errorMutableLiveData.value = R.string.modifyProducts__existingCode__error
                }else{
                    val product = ProductsType(name, amount.toInt(), price.toFloat())
                    myDictionary.setProduct(category, code, product)
                    confirmationMutableLiveData.value = true
                }
            }
        }
    }

    fun checkFields(code: String, category: String, isForSeeOperation: Boolean) {
        if(code.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyCode__error
        }else{
            var proceed = false
            var currentProduct: ProductsType? = null
            if(myDictionary.isSessionActive()){
                val user = myDictionary.getUser()
                if(myDataBase.containsProduct(user, category, code) == 1){
                    currentProduct = myDataBase.getProduct(user, category, code)
                    proceed = true
                }
            }
            else{
                if(myDictionary.containsProduct(category, code) == 1){
                    currentProduct = myDictionary.getProduct(category, code)
                    proceed = true
                }
            }
            if(proceed){
                val arrayCategories = getUpdatedCategories()
                auxProduct.name = currentProduct!!.name
                auxProduct.price = currentProduct.price
                auxProduct.amount = currentProduct.amount
                auxProduct.code = code
                auxProduct.category = arrayCategories.indexOf(category)
                if(isForSeeOperation){
                    confirmationMutableLiveData.value = true}
                else{
                    confirmationUploadChangeMutableLiveData.value = true
                }
            }else{
                errorMutableLiveData.value = R.string.modifyProducts__notExistingCode__error
            }
        }
    }

    fun checkLeftFieldsForChange(newCode: String, newName: String, newAmount: String, newPrice: String, newCategory: String){

    }
    fun uploadForChange(code: String, category: String){
        checkFields(code, category, false)
    }

    fun seeOperation(code: String, category: String){
        checkFields(code, category, true)
    }

    //--------------------------------------
    //Spinner
    //--------------------------------------
    private fun getUpdatedCategoriesDictionary(): ArrayList<String>{
        val categories = myDictionary.getCategories()
        val categoryList: ArrayList<String> = ArrayList()
        categoryList.addAll(categories)
        return categoryList
    }
    private fun getUpdatedCategoriesDataBase():ArrayList<String>{
        val categories = myDataBase.getCategories(myDictionary.getUser())
        val categoryList: ArrayList<String> = ArrayList()
        categoryList.addAll(categories!!)
        return categoryList
    }

    var getUpdatedCategories = ::getUpdatedCategoriesDictionary

    fun linkFunctionGetUpdatedCategoriesToDictionary(){
        getUpdatedCategories = ::getUpdatedCategoriesDictionary
    }

    fun linkFunctionGetUpdatedCategoriesToDataBase(){
        getUpdatedCategories = ::getUpdatedCategoriesDataBase
    }
}