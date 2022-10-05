package com.juanguicj.inventa_tu_tienda.main

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.R
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    fun database() {
        viewModelScope.launch {
            myDictionary.create()
            isSessionActive()
        }
    }

    private val productChangeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val productChangeLiveData: MutableLiveData<Boolean> = productChangeMutableLiveData
    private val categoryChangeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val categoryChangeLiveData: MutableLiveData<Boolean> = categoryChangeMutableLiveData
    private val logInMainMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val logInMainLiveData: LiveData<Boolean> = logInMainMutableLiveData

    private val isSessionActive_Coroutine_MutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isSessionActive_Coroutine_LiveData: LiveData<Boolean> = isSessionActive_Coroutine_MutableLiveData

    fun setCategoryChange(){
        categoryChangeMutableLiveData.value = true
    }

    fun setProductChange() {
        productChangeMutableLiveData.value = true
    }

    fun setSimpleLogin(){
        logInMainMutableLiveData.value = true
    }

    fun setSignUp(user: String, context: Context, builder: AlertDialog.Builder?){
        viewModelScope.launch {
            myDictionary.setUser(user)
            myDictionary.clearCategory()
            val db = Firebase.firestore
            db.collection(categoryTableCloudDatabase)
                .document(user)
                .set(hashMapOf("0" to arrayListOf(defaultTableCloudDatabaseName)))
                .addOnSuccessListener {
                    db.collection(productTableCloudDatabase)
                        .document(user)
                        .collection(defaultTableCloudDatabaseName)
                        .document("0")
                        .set(ProductsType("0", 0, 0.0f))
                        .addOnFailureListener{
                            clearLoginMain()
                            showDialog_DataBaseError(context, builder)
                        }
                }
                .addOnFailureListener {
                    clearLoginMain()
                    showDialog_DataBaseError(context, builder)
                }

            logInMainMutableLiveData.value = true
        }
    }

    fun setLogin(user: String){
        viewModelScope.launch {
            myDictionary.setUser(user)
            myDictionary.clearCategory()
            logInMainMutableLiveData.value = true
        }
    }

    fun clearLoginMain(){
        auth.signOut()
        viewModelScope.launch {
            myDictionary.clearUser()
            myDictionary.clearCategory()
            logInMainMutableLiveData.value = false
        }
    }

    fun getCategoriesFromDataBase(context: Context, builder: AlertDialog.Builder?){
        val db = Firebase.firestore
        viewModelScope.launch {
            db.collection(categoryTableCloudDatabase)
                .document(myDictionary.getUser())
                .get()
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        categories.clear()
                        val list: Any? = task.result.getField("0")
                        if(list is ArrayList<*>){
                            list.forEach { cat ->
                                categories.add(cat.toString())
                            }
                            setCategoryChange()
                        }else{
                            showDialog_DataBaseError(context, builder)
                        }
                    }else{
                        showDialog_DataBaseError(context, builder)
                    }
                }
        }
    }

    fun getCategoriesFromDictionary(){
        viewModelScope.launch {
            val categoriesList = myDictionary.getCategories()
            categories.clear()
            categories.addAll(categoriesList)
            setCategoryChange()
        }
    }

    fun isSessionActive() {
        viewModelScope.launch {
            isSessionActive_Coroutine_MutableLiveData.value = myDictionary.isSessionActive()
        }
    }
}