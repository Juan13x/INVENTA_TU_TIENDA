package com.juanguicj.inventa_tu_tienda.fragments.user.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.myDataBase
import com.juanguicj.inventa_tu_tienda.main.myDictionary

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

    fun checkChange(currentPassword: String, newPassword: String, repNewPassword: String){
        fun isLongPasswordOK(password: String): Boolean{
            return password.length > 6
        }
        if(checkFields(currentPassword, newPassword, repNewPassword)){
            if(currentPassword != newPassword){
                if(myDataBase.isTheSamePassword(myDictionary.getUser(), currentPassword)){
                    if(newPassword == repNewPassword){
                        if(isLongPasswordOK(newPassword)){
                            successChangePasswordMutableLiveData.value = true
                        }
                        else{
                            warningWrongMutableLiveData.value = R.string.changePassword__passwordLengthWrong
                        }
                    }
                    else{
                        warningWrongMutableLiveData.value = R.string.changePassword__differentPasswords
                    }
                }else{
                    warningWrongMutableLiveData.value = R.string.changePassword__wrongCurrentPassword
                }
            }else{
                warningWrongMutableLiveData.value = R.string.changePassword__CurrentPasswordAndNewPasswordSame
            }
        }
    }
}
