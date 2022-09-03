package com.juanguicj.inventa_tu_tienda.fragments.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juanguicj.inventa_tu_tienda.fragments.modify.products.*
import com.juanguicj.inventa_tu_tienda.main.ProductsType
import com.juanguicj.inventa_tu_tienda.main.myDataBase
import com.juanguicj.inventa_tu_tienda.main.myDictionary

class ProductsViewModel : ViewModel() {
    private val errorCategoryMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorCategoryLiveData: LiveData<Boolean> = errorCategoryMutableLiveData
    private val errorSystemMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorSystemLiveData: LiveData<Boolean> = errorSystemMutableLiveData
    private val errorProductMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorProductLiveData: LiveData<Boolean> = errorProductMutableLiveData
    private val afterSelectionConfirmationMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val afterSelectionConfirmationLiveData: LiveData<Boolean> = afterSelectionConfirmationMutableLiveData


    fun getCategoryList(): MutableList<String>?{
        val categories = if(myDictionary.isSessionActive()){
            myDataBase.getCategories(myDictionary.getUser())?.toMutableList()
        }else{
            myDictionary.getCategories().toMutableList()
        }
        if(categories.isNullOrEmpty()){
            errorCategoryMutableLiveData.value = true
            return null
        }
        return categories
    }

    fun checkCategoryField(category: Any){
        if(category is String){
            myDictionary.setCategory(category)
            afterSelectionConfirmationMutableLiveData.value = true
        }else{
            errorSystemMutableLiveData.value = true
        }
    }

    fun getProductList(productAdapter: ProductAdapter){
        val products: MutableMap<String, ProductsType>? = if(myDictionary.isSessionActive()){
            myDataBase.getAllProducts(myDictionary.getUser(), myDictionary.getCategory())
        }else{
            myDictionary.getAllProducts(myDictionary.getCategory())
        }
        if (products != null) {
            val array = arrayListOf<ArrayList<String?>>()
            val arrayToAdd = arrayListOf<String?>()
            arrayToAdd.addAll(arrayOf("","","",""))
            for(i_product in products.asIterable()){
                arrayToAdd[codePosition] = i_product.key
                arrayToAdd[namePosition] = i_product.value.name
                arrayToAdd[pricePosition] = i_product.value.price.toString()
                arrayToAdd[amountPosition] = i_product.value.amount.toString()
                array.add(arrayToAdd)
            }
            productAdapter.updateAll(array)
        }else{
            errorProductMutableLiveData.value = true
        }
    }
}