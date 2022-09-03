package com.juanguicj.inventa_tu_tienda.fragments.modify.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.myDataBase
import com.juanguicj.inventa_tu_tienda.main.myDictionary

class ModifyCategoryViewModel : ViewModel() {
    private val errorMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val errorLiveData: LiveData<Int> = errorMutableLiveData

    private val confirmationADDMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationADDLiveData: LiveData<Boolean> = confirmationADDMutableLiveData

    private val confirmationRENAMEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationRENAMELiveData: LiveData<Boolean> = confirmationRENAMEMutableLiveData

    private val confirmationERASEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationERASELiveData: LiveData<Boolean> = confirmationERASEMutableLiveData

    fun ADD_operation(nameCategory: String){
        if(nameCategory.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyRename__error
        }
        else{
            if(myDictionary.isSessionActive()){
                if(myDataBase.addCategory(myDictionary.getUser(), nameCategory)==1){
                    confirmationADDMutableLiveData.value = true
                }else{
                    errorMutableLiveData.value = R.string.modifyCategories__NewCategoryIncluded__error
                }
            }else{
                if(myDictionary.addCategory(nameCategory)){
                    confirmationADDMutableLiveData.value = true
                }else{
                    errorMutableLiveData.value = R.string.modifyCategories__NewCategoryIncluded__error
                }
            }
        }
    }

    fun RENAME_Operation(currentCategory: String, newName: String){
        if(currentCategory.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyCategory__error
        }
        else if(newName.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyRename__error
        }
        else if(newName == currentCategory){
            errorMutableLiveData.value = R.string.modifyCategories__RenameToSameCategory__error
        }
        else{
            if(myDictionary.isSessionActive()){
                if(myDataBase.renameCategory(myDictionary.getUser(), currentCategory, newName)==1){
                    confirmationRENAMEMutableLiveData.value = true
                }else{
                    errorMutableLiveData.value = R.string.modifyCategories__RenameCategoryIncluded__error
                }
            }else{
                if(myDictionary.renameCategory(currentCategory, newName) == 1){
                    confirmationRENAMEMutableLiveData.value = true
                }else{
                    errorMutableLiveData.value = R.string.modifyCategories__RenameCategoryIncluded__error
                }
            }
        }
    }

    fun checkFields_for_ERASE(selectedCategory: String){
        if(selectedCategory.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyCategory__error
        }
        else{
            confirmationERASEMutableLiveData.value = true
        }
    }

    fun DELETE_operation(selectedCategory: String){
        if(myDictionary.isSessionActive()){
            myDataBase.deleteCategory(myDictionary.getUser(),selectedCategory)
        }else{
            myDictionary.deleteCategory(selectedCategory)
        }
    }
}