package com.juanguicj.inventa_tu_tienda.fragments.user.login

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.auth
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import com.juanguicj.inventa_tu_tienda.main.showDialog_DataBaseError
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {
    private val warningMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val warningLiveData: LiveData<Int> = warningMutableLiveData
    private val successLogInMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successLogInLiveData: LiveData<Boolean> = successLogInMutableLiveData

    fun checkFieldsLogIn(user: String, password: String): Boolean{
        return user.isNotEmpty().and(password.isNotEmpty())
    }

    fun logInOperation(user: String, password: String, context: Context, builder: AlertDialog.Builder?) {
        if(checkFieldsLogIn(user, password)){
            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        viewModelScope.launch {
                            myDictionary.setUser(user)
                            successLogInMutableLiveData.value = true
                        }
                    }else{
                        try {
                            throw task.exception!!
                        } catch(e : FirebaseAuthException){
                            Log.e("errorCode", e.errorCode)
                            Log.e("errorMessage", e.message ?: "")
                            when (e.errorCode) {
                                "ERROR_USER_NOT_FOUND"-> {
                                    warningMutableLiveData.value = R.string.logIn__warningNotExistingUser
                                }
                                "ERROR_WRONG_PASSWORD" -> {
                                    warningMutableLiveData.value = R.string.logIn__warningWrongPassword
                                }
                                "ERROR_INVALID_EMAIL"->{
                                    warningMutableLiveData.value = R.string.database_invalidEmail
                                }
                                else -> {
                                    showDialog_DataBaseError(context, builder)
                                }
                            }
                        }

                    }
                }
        }
        else{
            warningMutableLiveData.value = R.string.logIn__warningEmpty
        }
    }
}