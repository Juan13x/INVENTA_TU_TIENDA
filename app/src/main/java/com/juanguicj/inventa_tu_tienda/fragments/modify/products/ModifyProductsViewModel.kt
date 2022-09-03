package com.juanguicj.inventa_tu_tienda.fragments.modify.products

import android.content.Intent
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
                           var price: String,
                           var amount: String,
                           var categorySTR: String,
                           var category: Int)
    private var auxProduct = ProductInfo(name = "", code = "", price = "", amount = "", category = 0, categorySTR = "")
    private var productForChange = ProductInfo(name = "", code = "", price = "", amount = "", category = 0, categorySTR = "")

    fun getAuxProduct(): ProductInfo{
        return auxProduct
    }
    fun getProductToChange(): ProductInfo{
        return productForChange
    }

    private val confirmationUploadCHANGEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationUploadCHANGELiveData: LiveData<Boolean> = confirmationUploadCHANGEMutableLiveData

    val confirmationADDMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationADDLiveData: LiveData<Boolean> = confirmationADDMutableLiveData
    val confirmationSEEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationSEELiveData: LiveData<Boolean> = confirmationSEEMutableLiveData
    val confirmationCHANGEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationCHANGELiveData: LiveData<Boolean> = confirmationCHANGEMutableLiveData
    val confirmationDELETEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationDELETELiveData: LiveData<Boolean> = confirmationDELETEMutableLiveData

    private val errorMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val errorLiveData: LiveData<Int> = errorMutableLiveData

    fun addOperation(code: String, name: String, amount: String, price: String, category: String){
        fun checkFieldAddOperation(code: String, name: String, amount: String, price: String):
                Boolean{
            if(category.isEmpty()){
                errorMutableLiveData.value = R.string.modifyProducts__emptyCategory__error
            }
            else if(code.isEmpty()){
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
                    confirmationADDMutableLiveData.value = true
                }
            }
            else{
                if(myDictionary.containsProduct(category, code) == 1){
                    errorMutableLiveData.value = R.string.modifyProducts__existingCode__error
                }else{
                    val product = ProductsType(name, amount.toInt(), price.toFloat())
                    myDictionary.setProduct(category, code, product)
                    confirmationADDMutableLiveData.value = true
                }
            }
        }
    }

    fun checkFields(code: String, category: String, categoryPosition: Int, whichOperation: Int) {
        if(category.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyCategory__error
        }
        else if(code.isEmpty()){
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
                auxProduct.name = currentProduct!!.name
                auxProduct.price = currentProduct.price.toString()
                auxProduct.amount = currentProduct.amount.toString()
                if(whichOperation == 0){
                    confirmationSEEMutableLiveData.value = true
                }
                else{
                    auxProduct.code = code
                    auxProduct.categorySTR = category
                    if(whichOperation == 1){
                        auxProduct.category = categoryPosition
                        confirmationUploadCHANGEMutableLiveData.value = true
                    }
                    else{
                        confirmationDELETEMutableLiveData.value = true
                    }
                }
            }else{
                errorMutableLiveData.value = R.string.modifyProducts__notExistingCode__error
            }
        }
    }

    fun changeOperation(){
        val newProduct = ProductsType(productForChange.name, productForChange.amount.toInt(), productForChange.price.toFloat())
        if(myDictionary.isSessionActive()){
            myDataBase.deleteProduct(myDictionary.getUser(), auxProduct.categorySTR, auxProduct.code)
            myDataBase.setProduct(myDictionary.getUser(), productForChange.categorySTR, productForChange.code, newProduct)
        }else{
            myDictionary.deleteProduct(auxProduct.categorySTR, auxProduct.code)
            myDictionary.setProduct(productForChange.categorySTR, productForChange.code, newProduct)
        }
    }
    fun checkFieldsForChange(newCode: String, newName: String, newAmount: String, newPrice: String, newCategory: String, newCategoryPosition: Int){
        fun checkBelonging(): Int{
            val belonging = if(myDictionary.isSessionActive()){
                myDataBase.containsProduct(myDictionary.getUser(), newCategory, newCode)
            }else{
                myDictionary.containsProduct(newCategory, newCode)
            }
            return belonging
        }
        if(newCode.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyNewCode__error
        }
        else if(newName.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyNewName__error
        }
        else if(newPrice.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyNewPrice__error
        }
        else if(newAmount.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyNewAmount__error
        }
        else{
            var proceed = true
            if((newCode != auxProduct.code).and(newCategoryPosition == auxProduct.category)){
                proceed = checkBelonging() == 0
                if(!proceed){
                    errorMutableLiveData.value = R.string.modifyProducts__existingNewCodeSameCategory__error
                }
            }
            else if(newCategoryPosition != auxProduct.category){
                proceed = checkBelonging() == 0
                if(!proceed){
                    errorMutableLiveData.value = R.string.modifyProducts__existingNewCodeDifferentCategory__error
                }
            }
            if(proceed){
                productForChange.name = newName
                productForChange.code = newCode
                productForChange.price = newPrice
                productForChange.amount = newAmount
                productForChange.category = newCategoryPosition
                productForChange.categorySTR = newCategory
                confirmationCHANGEMutableLiveData.value = true
            }
        }
    }
    fun uploadForChange(code: String, category: String, categoryPosition: Int){
        checkFields(code, category, categoryPosition, 1)
    }

    fun seeOperation(code: String, category: String, categoryPosition: Int){
        checkFields(code, category, categoryPosition, 0)
    }

    fun deleteOperation(){
        if(myDictionary.isSessionActive()){
            myDataBase.deleteProduct(myDictionary.getUser(), auxProduct.categorySTR, auxProduct.code)
        }else{
            myDictionary.deleteProduct(auxProduct.categorySTR, auxProduct.code)
        }
    }
}