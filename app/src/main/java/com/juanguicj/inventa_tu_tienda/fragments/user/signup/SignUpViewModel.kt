package com.juanguicj.inventa_tu_tienda.fragments.user.signup

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.auth
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import com.juanguicj.inventa_tu_tienda.main.showDialog_DataBaseError
import kotlinx.coroutines.launch

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

    fun signUp(user: String, password: String, repPassword: String, context: Context, builder: AlertDialog.Builder?){
        fun isLongPasswordOK(password: String): Boolean{
            return password.length > 6
        }
        if(checkFields(user, password, repPassword)){
            if(checkEmail(user)){
                if(password == repPassword){
                    if(isLongPasswordOK(password)){
                        val db = Firebase.firestore
                        auth.createUserWithEmailAndPassword(user, password)
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    viewModelScope.launch {
                                        myDictionary.setUser(user)
                                        successSignUpMutableLiveData.value = true
                                    }
                                } else{
                                    try{
                                        throw task.exception!!
                                    }catch (e: FirebaseAuthException){
                                        val errorCode = e.errorCode
                                        val errorMessage = e.message
                                        Log.e("errorCode", errorCode)
                                        Log.e("errorMessage", errorMessage ?: "")
                                        when(errorCode){
                                            "ERROR_INVALID_EMAIL" -> warningWrongMutableLiveData.value = R.string.signUp__wrongEmailAddress
                                            "ERROR_EMAIL_ALREADY_IN_USE" -> warningWrongMutableLiveData.value = R.string.signUp__existingUser
                                            else -> showDialog_DataBaseError(context, builder)
                                        }
                                    }
                                }
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