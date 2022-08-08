package com.juanguicj.inventa_tu_tienda.fragments.user.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.myDataBase

class SignUpViewModel : ViewModel() {
    private val warningUserMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val warningUserLiveData: LiveData<Boolean> = warningUserMutableLiveData
    private val warningPasswordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val warningPasswordLiveData: LiveData<Boolean> = warningPasswordMutableLiveData
    private val warningRepeatPasswordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val warningRepeatPasswordLiveData: LiveData<Boolean> = warningRepeatPasswordMutableLiveData

    private val warningWrongMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val warningWrongLiveData: LiveData<Int> = warningWrongMutableLiveData

    private val successSignUpMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successSignUpLiveData: LiveData<Boolean> = successSignUpMutableLiveData

    private fun checkFields(user: String, password: String, repPassword: String): Boolean{
        if(user.isEmpty()){
            warningUserMutableLiveData.value = true
        }else if(password.isEmpty()){
            warningPasswordMutableLiveData.value = true
        }
        else if(repPassword.isEmpty()){
            warningRepeatPasswordMutableLiveData.value = true
        }else{
            return true
        }
        return false
    }

    private fun checkEmail(email: String): Boolean{
        fun countOccurrences(s: String, ch: Char): Int {
            return s.count { it == ch }
        }

        if(countOccurrences(email, '@') == 1){
            val indexAT = email.indexOf("@")
            if(countOccurrences(email, '.') == 1){
                val indexPoint = email.indexOf(".")
                if(indexAT < indexPoint){
                    return true
                }

            }
        }
        return false
    }

    fun checkSignUp(user: String, password: String, repPassword: String){
        fun isLongPasswordOK(password: String): Boolean{
            return password.length > 6
        }
        if(checkFields(user, password, repPassword)){
            if(checkEmail(user)){
                if(password == repPassword){
                    if(isLongPasswordOK(password)){
                        if(!myDataBase.containsUser(user)){
                            successSignUpMutableLiveData.value = true
                        } else{
                            warningWrongMutableLiveData.value = R.string.signUp__existingUser
                        }
                    }
                    else{
                        warningWrongMutableLiveData.value = R.string.signUp__warningShortPassword
                    }
                }
                else{
                    warningWrongMutableLiveData.value = R.string.signUp__differentPasswords
                }
            }
            else{
                warningWrongMutableLiveData.value = R.string.signUp__wrongEmailAddress
            }
        }
    }
}