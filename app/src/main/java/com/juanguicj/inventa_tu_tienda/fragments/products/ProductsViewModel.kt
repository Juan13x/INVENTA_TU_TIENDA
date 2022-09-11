package com.juanguicj.inventa_tu_tienda.fragments.products

import android.R
import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.fragments.modify.products.*
import com.juanguicj.inventa_tu_tienda.main.ProductsType
import com.juanguicj.inventa_tu_tienda.main.myDataBase
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import com.juanguicj.inventa_tu_tienda.main.showDialog_DataBaseError

class ProductsViewModel : ViewModel() {
    private val errorCategoryMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorCategoryLiveData: LiveData<Boolean> = errorCategoryMutableLiveData
    private val errorSystemMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorSystemLiveData: LiveData<Boolean> = errorSystemMutableLiveData
    private val errorProductMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorProductLiveData: LiveData<Boolean> = errorProductMutableLiveData
    private val afterSelectionConfirmationMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val afterSelectionConfirmationLiveData: LiveData<Boolean> = afterSelectionConfirmationMutableLiveData


    fun getCategoryList(listView: ListView, context: Context){
        val categories = if(myDictionary.isSessionActive()){
            myDataBase.getCategories(myDictionary.getUser())?.toMutableList()
        }else{
            myDictionary.getCategories().toMutableList()
        }
        if(categories.isNullOrEmpty()){
            errorCategoryMutableLiveData.value = true
            listView.adapter = null
        } else{
            val adapter = ArrayAdapter(context, R.layout.simple_list_item_1, categories)
            listView.adapter = adapter
        }
    }

    fun checkCategoryField(category: Any){
        if(category is String){
            myDictionary.setCategory(category)
            afterSelectionConfirmationMutableLiveData.value = true
        }else{
            errorSystemMutableLiveData.value = true
        }
    }

    fun getProductList(productAdapter: ProductAdapter, context: Context, builder: AlertDialog.Builder?){
        fun assignProductsFromDocumentSnapshot(products: MutableList<DocumentSnapshot>){
            if (products.isNotEmpty()) {
                var array = arrayOf<Array<String?>>()
                for(i_product in products.asIterable()){
                    val arrayToAdd : Array<String?> = arrayOfNulls(4)
                    arrayToAdd[codePosition] = i_product.id
                    arrayToAdd[namePosition] = i_product.getString("name")
                    arrayToAdd[pricePosition] = i_product.getDouble("price").toString()
                    arrayToAdd[amountPosition] = i_product.getLong("amount").toString()
                    array += arrayToAdd
                }
                productAdapter.updateAll(array)
            }else{
                errorProductMutableLiveData.value = true
            }
        }

        fun assignProductsFromMutableMap(products: MutableMap<String, ProductsType>?){
            if (!products.isNullOrEmpty()) {
                var array = arrayOf<Array<String?>>()
                for(i_product in products.asIterable()){
                    val arrayToAdd : Array<String?> = arrayOfNulls(4)
                    arrayToAdd[codePosition] = i_product.key
                    arrayToAdd[namePosition] = i_product.value.name
                    arrayToAdd[pricePosition] = i_product.value.price.toString()
                    arrayToAdd[amountPosition] = i_product.value.amount.toString()
                    array += arrayToAdd
                }
                productAdapter.updateAll(array)
            }else{
                errorProductMutableLiveData.value = true
            }
        }

        var products: MutableList<DocumentSnapshot>
        if(myDictionary.isSessionActive()){
            val db = Firebase.firestore
            db.collection("products")
                .document(myDictionary.getUser())
                .collection(myDictionary.getCategory())
                .get().addOnSuccessListener { query ->
                    products = query.documents
                    assignProductsFromDocumentSnapshot(products)
                }.addOnFailureListener{
                    showDialog_DataBaseError(context, builder)
                }
        }else{
            val productFromDictionary = myDictionary.getAllProducts(myDictionary.getCategory())
            assignProductsFromMutableMap(productFromDictionary)
        }
    }
}