package com.juanguicj.inventa_tu_tienda.fragments.modify.products

import android.app.AlertDialog
import android.content.Context
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.*
import kotlinx.coroutines.launch

class ModifyProductsViewModel : ViewModel() {
    data class ProductInfo(var code: String,
                           var name: String,
                           var price: String,
                           var amount: String,
                           var categorySTR: String,
                           var category: Int)
    private var auxProduct = ProductInfo(name = "", code = "", price = "", amount = "", category = 0, categorySTR = "")
    private var productForChange = ProductInfo(name = "", code = "", price = "", amount = "", category = 0, categorySTR = "")
    private var typeOfChange = false //false for change as upload, true for change as delete and set

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

    fun addOperation(code: String, name: String, amount: String, price: String, category: Spinner, context: Context, builder: AlertDialog.Builder?){
        fun checkFieldAddOperation(code: String, name: String, amount: String, price: String):
                Boolean{
            if(category.selectedItem == null){
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
            viewModelScope.launch() {
                if (myDictionary.isSessionActive()) {
                    val user = myDictionary.getUser()
                    val db = Firebase.firestore
                    val query = db.collection("products")
                        .document(user)
                        .collection(category.selectedItem.toString())
                        .document(code)
                    query.get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                errorMutableLiveData.value = R.string.modifyProducts__existingCode__error
                            } else {
                                query.set(ProductsType(name, amount.toInt(), price.toFloat()))
                                confirmationADDMutableLiveData.value = true
                            }
                        }.addOnFailureListener {
                            showDialog_DataBaseError(context, builder)
                        }
                } else {
                    if (myDictionary.containsProduct(category.selectedItem.toString(), code) == 1) {
                        errorMutableLiveData.value = R.string.modifyProducts__existingCode__error
                    } else {
                        val product = ProductsType(name, amount.toInt(), price.toFloat())
                        myDictionary.setProduct(category.selectedItem.toString(), code, product)
                        confirmationADDMutableLiveData.value = true
                    }
                }
            }
        }
    }

    fun checkFields(code: String, category: Spinner, categoryPosition: Int, whichOperation: Int, context: Context, builder: AlertDialog.Builder?) {
        fun proceed(currentProduct: ProductsType?, categorySTR: String){
            auxProduct.name = currentProduct!!.name
            auxProduct.price = currentProduct.price.toString()
            auxProduct.amount = currentProduct.amount.toString()
            if(whichOperation == 0){
                confirmationSEEMutableLiveData.value = true
            }
            else{
                auxProduct.code = code
                auxProduct.categorySTR = categorySTR
                if(whichOperation == 1){
                    auxProduct.category = categoryPosition
                    confirmationUploadCHANGEMutableLiveData.value = true
                }
                else{
                    confirmationDELETEMutableLiveData.value = true
                }
            }
        }
        if(category.selectedItem == null){
            errorMutableLiveData.value = R.string.modifyProducts__emptyCategory__error
        }
        else if(code.isEmpty()){
            errorMutableLiveData.value = R.string.modifyProducts__emptyCode__error
        }else{
            viewModelScope.launch() {
                val currentProduct: ProductsType?
                val categorySTR = category.selectedItem.toString()

                if (myDictionary.isSessionActive()) {
                    val user = myDictionary.getUser()
                    val db = Firebase.firestore
                    val query = db.collection("products")
                        .document(user)
                        .collection(categorySTR)
                        .document(code)
                    query.get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val name: String = document.getString("name") ?: ""
                                val amount: Int = document.getLong("amount")?.toInt() ?: 0
                                val price: Float = document.getDouble("price")?.toFloat() ?: 0.0f
                                proceed(ProductsType(name, amount, price), categorySTR)
                            } else {
                                errorMutableLiveData.value = R.string.modifyProducts__notExistingCode__error
                            }
                        }.addOnFailureListener {
                            showDialog_DataBaseError(context, builder)
                        }
                    } else {
                        if (myDictionary.containsProduct(categorySTR, code) == 1) {
                            currentProduct = myDictionary.getProduct(categorySTR, code)
                            proceed(currentProduct, categorySTR)
                        } else {
                            errorMutableLiveData.value = R.string.modifyProducts__notExistingCode__error
                        }
                    }
            }
        }
    }

    fun changeOperation(context: Context, builder: AlertDialog.Builder?) {
        val newProduct = ProductsType(productForChange.name, productForChange.amount.toInt(), productForChange.price.toFloat())
        viewModelScope.launch() {
            if (myDictionary.isSessionActive()) {
                val db = Firebase.firestore

                if (!typeOfChange) {
                    db.collection("products").document(myDictionary.getUser())
                        .collection(auxProduct.categorySTR)
                        .document(auxProduct.code)
                        .set(newProduct)
                        .addOnFailureListener {
                            showDialog_DataBaseError(context, builder)
                        }
                } else {
                    db.collection("products").document(myDictionary.getUser())
                        .collection(auxProduct.categorySTR)
                        .document(auxProduct.code)
                        .delete()
                        .addOnSuccessListener {
                            viewModelScope.launch() {
                                db.collection("products").document(myDictionary.getUser())
                                    .collection(productForChange.categorySTR)
                                    .document(productForChange.code)
                                    .set(newProduct)
                                    .addOnFailureListener {
                                        showDialog_DataBaseError(context, builder)
                                    }
                            }
                        }
                        .addOnFailureListener {
                            showDialog_DataBaseError(context, builder)
                        }
                }
            } else {
                myDictionary.deleteProduct(auxProduct.categorySTR, auxProduct.code)
                myDictionary.setProduct(productForChange.categorySTR, productForChange.code, newProduct)
            }
        }
    }
    fun checkFieldsForChange(newCode: String, newName: String, newAmount: String, newPrice: String, newCategory: String, newCategoryPosition: Int, context: Context, builder: AlertDialog.Builder?){
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
            viewModelScope.launch {
                if (myDictionary.isSessionActive()) {
                    if ((newCode == auxProduct.code).and(newCategoryPosition == auxProduct.category)) {
                        typeOfChange = false
                        productForChange.code = newCode
                        productForChange.name = newName
                        productForChange.price = newPrice
                        productForChange.amount = newAmount
                        productForChange.category = newCategoryPosition
                        productForChange.categorySTR = newCategory
                        confirmationCHANGEMutableLiveData.value = true
                    } else {
                        val db = Firebase.firestore
                        val query = db.collection("products")
                            .document(myDictionary.getUser())
                            .collection(newCategory)
                            .document(newCode)
                        query.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    if ((newCode != auxProduct.code).and(newCategoryPosition == auxProduct.category)) {
                                        errorMutableLiveData.value = R.string.modifyProducts__existingNewCodeSameCategory__error
                                    } else {
                                        errorMutableLiveData.value = R.string.modifyProducts__existingNewCodeDifferentCategory__error
                                    }
                                } else {
                                    typeOfChange = true
                                    productForChange.name = newName
                                    productForChange.code = newCode
                                    productForChange.price = newPrice
                                    productForChange.amount = newAmount
                                    productForChange.category = newCategoryPosition
                                    productForChange.categorySTR = newCategory
                                    confirmationCHANGEMutableLiveData.value = true
                                }
                            }.addOnFailureListener {
                                showDialog_DataBaseError(context, builder)
                            }
                    }
                } else {
                    var proceed = true
                    if ((newCode != auxProduct.code).and(newCategoryPosition == auxProduct.category)) {
                        proceed = myDictionary.containsProduct(newCategory, newCode) == 0
                        if (!proceed) {
                            errorMutableLiveData.value = R.string.modifyProducts__existingNewCodeSameCategory__error
                        }
                    } else if (newCategoryPosition != auxProduct.category) {
                        proceed = myDictionary.containsProduct(newCategory, newCode) == 0
                        if (!proceed) {
                            errorMutableLiveData.value = R.string.modifyProducts__existingNewCodeDifferentCategory__error
                        }
                    }
                    if (proceed) {
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
        }
    }
    fun uploadForChange(code: String, category: Spinner, categoryPosition: Int, context: Context, builder: AlertDialog.Builder?){
        checkFields(code, category, categoryPosition, 1, context, builder)
    }

    fun seeOperation(code: String, category: Spinner, categoryPosition: Int, context: Context, builder: AlertDialog.Builder?){
        checkFields(code, category, categoryPosition, 0, context, builder)
    }

    fun deleteOperation(context: Context, builder: AlertDialog.Builder?){
        viewModelScope.launch() {
            if(myDictionary.isSessionActive()){
                val db = Firebase.firestore
                db.collection("products").document(myDictionary.getUser())
                    .collection(auxProduct.categorySTR)
                    .document(auxProduct.code)
                    .delete()
                    .addOnFailureListener {
                        showDialog_DataBaseError(context, builder)
                    }
            }else{
                myDictionary.deleteProduct(auxProduct.categorySTR, auxProduct.code)
            }
        }
    }
}