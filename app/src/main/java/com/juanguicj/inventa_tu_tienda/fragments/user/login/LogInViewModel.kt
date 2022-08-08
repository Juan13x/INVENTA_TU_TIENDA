package com.juanguicj.inventa_tu_tienda.fragments.user.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.myDataBase

class LogInViewModel : ViewModel() {
    private val warningMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val warningLiveData: LiveData<Int> = warningMutableLiveData
    private val successLogInMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successLogInLiveData: LiveData<Boolean> = successLogInMutableLiveData

    fun checkLogIn(user: String, password: String){
        if(user.isNotEmpty().and(password.isNotEmpty())){
            if(myDataBase.containsUser(user)){
                if(myDataBase.isTheSamePassword(user, password)){
                    successLogInMutableLiveData.value = true
                } else{
                    warningMutableLiveData.value = R.string.logIn__warningWrongPassword
                }
            } else{
                warningMutableLiveData.value = R.string.logIn__warningNotExistingUser
            }
        }
        else{
            warningMutableLiveData.value = R.string.logIn__warningEmpty
        }
    }
}