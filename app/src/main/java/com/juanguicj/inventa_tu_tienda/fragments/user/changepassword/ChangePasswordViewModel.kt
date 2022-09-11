package com.juanguicj.inventa_tu_tienda.fragments.user.changepassword

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseError
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.auth
import com.juanguicj.inventa_tu_tienda.main.myDataBase
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import com.juanguicj.inventa_tu_tienda.main.showDialog_DataBaseError

class ChangePasswordViewModel : ViewModel() {
    var errorMessage: String = ""
    fun getError(): String{
        return errorMessage
    }


    private val warningCurrentPasswordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val warningCurrentPasswordLiveData: LiveData<Boolean> = warningCurrentPasswordMutableLiveData
    private val warningNewPasswordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val warningNewPasswordLiveData: LiveData<Boolean> = warningNewPasswordMutableLiveData
    private val warningRepeatNewPasswordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val warningRepeatNewPasswordLiveData: LiveData<Boolean> = warningRepeatNewPasswordMutableLiveData
    private val warningWrongMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val warningWrongLiveData: LiveData<Int> = warningWrongMutableLiveData
    private val successChangePasswordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successChangePasswordLiveData: LiveData<Boolean> = successChangePasswordMutableLiveData

    private fun checkFields(currentPassword: String, NewPassword: String, repNewPassword: String):
            Boolean{
        if(currentPassword.isEmpty()){
            warningCurrentPasswordMutableLiveData.value = true
        }else if(NewPassword.isEmpty()){
            warningNewPasswordMutableLiveData.value = true
        }
        else if(repNewPassword.isEmpty()){
            warningRepeatNewPasswordMutableLiveData.value = true
        }else{
            return true
        }
        return false
    }

    fun checkChange(currentPassword: String, newPassword: String, repNewPassword: String, context: Context, builder: AlertDialog.Builder?){
        fun isLongPasswordOK(password: String): Boolean{
            return password.length > 6
        }
        if(checkFields(currentPassword, newPassword, repNewPassword)){
            if(currentPassword != newPassword){
                if(newPassword == repNewPassword){
                    if(isLongPasswordOK(newPassword)){
                        val user = auth.currentUser!! //TODO() = consider the user is not signedIn anymore
                        val credentials = EmailAuthProvider.getCredential(myDictionary.getUser(), currentPassword)
                        user.reauthenticate(credentials)
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    user.updatePassword(newPassword).addOnSuccessListener {
                                        successChangePasswordMutableLiveData.value = true
                                    }.addOnFailureListener{
                                        showDialog_DataBaseError(context, builder)
                                    }

                                }
                                else{
                                    try {
                                        throw task.exception!!
                                    }catch(e: FirebaseAuthException){
                                        val errorCode = e.errorCode
                                        val errorMessage = e.message
                                        Log.e("errorCode", errorCode)
                                        Log.e("errorMessage", errorMessage ?: "")
                                        when(errorCode){
                                            "ERROR_INVALID_CREDENTIAL" -> warningWrongMutableLiveData.value = R.string.changePassword__wrongCurrentPassword
                                            else -> showDialog_DataBaseError(context, builder)
                                        }
                                    }
                                }
                            }
                    }
                    else{
                        warningWrongMutableLiveData.value = R.string.changePassword__passwordLengthWrong
                    }
                }else{
                    warningWrongMutableLiveData.value = R.string.changePassword__differentPasswords
                }
            }else{
                warningWrongMutableLiveData.value = R.string.changePassword__CurrentPasswordAndNewPasswordSame
            }
        }
    }
}
