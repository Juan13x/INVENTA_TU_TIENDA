package com.juanguicj.inventa_tu_tienda.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun setSignUp(user: String, password:String){
        myDataBase.addUser(user)
        myDataBase.setPassword(user, password)
        setLogin(user)
    }

    fun clearLoginMain(){
        myDictionary.clearUser()
        myDictionary.clearCategory()
        logInMainMutableLiveData.value = false
    }

    fun setNewPassword(user: String, newPassword: String) {
        myDataBase.setPassword(user, newPassword)
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