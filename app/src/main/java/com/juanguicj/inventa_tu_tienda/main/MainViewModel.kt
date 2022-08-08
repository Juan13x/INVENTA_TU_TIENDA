package com.juanguicj.inventa_tu_tienda.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val logInMainMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val logInMainLiveData: LiveData<Boolean> = logInMainMutableLiveData

    fun setLogin(user: String){
        myDictionary.setUser(user)
        logInMainMutableLiveData.value = true
    }

    fun setSignUp(user: String, password:String){
        myDictionary.setUser(user)
        myDataBase.addUser(user)
        myDataBase.setPassword(user, password)
        logInMainMutableLiveData.value = true
    }

    fun clearLoginMain(){
        myDictionary.clearUser()
        myDictionary.clearCategory()
        logInMainMutableLiveData.value = false
    }

    fun setNewPassword() {

    }
}