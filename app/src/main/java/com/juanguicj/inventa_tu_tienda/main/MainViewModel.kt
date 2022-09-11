package com.juanguicj.inventa_tu_tienda.main

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class MainViewModel: ViewModel() {
    private val productChangeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val productChangeLiveData: MutableLiveData<Boolean> = productChangeMutableLiveData
    private val categoryChangeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val categoryChangeLiveData: MutableLiveData<Boolean> = categoryChangeMutableLiveData
    private val logInMainMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val logInMainLiveData: LiveData<Boolean> = logInMainMutableLiveData


    fun setCategoryChange(){
        categoryChangeMutableLiveData.value = true
    }

    fun setLogin(user: String){
        myDictionary.setUser(user)
        myDictionary.clearCategory()
        logInMainMutableLiveData.value = true
    }

    fun clearLoginMain(){
        auth.signOut()
        myDictionary.clearUser()
        myDictionary.clearCategory()
        logInMainMutableLiveData.value = false
    }

    fun getCategoriesFromDataBase(context: Context, builder: AlertDialog.Builder?){
        val db = Firebase.firestore
        db.collection("categories")
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

    fun getCategoriesFromDictionary(){
        val categoriesList = myDictionary.getCategories()
        categories.clear()
        categories.addAll(categoriesList)
        setCategoryChange()
    }
}