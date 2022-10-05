package com.juanguicj.inventa_tu_tienda.fragments.modify.category

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseError
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.main.*
import kotlinx.coroutines.launch

class ModifyCategoryViewModel : ViewModel() {
    private val errorMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val errorLiveData: LiveData<Int> = errorMutableLiveData

    private val simpleConfirmationMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val simpleConfirmationLiveData: LiveData<Int> = simpleConfirmationMutableLiveData

    private val confirmationERASEMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val confirmationERASELiveData: LiveData<Boolean> = confirmationERASEMutableLiveData

    fun ADD_operation(nameCategory: String, context: Context, builder: AlertDialog.Builder?){
        if(nameCategory.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyRename__error
        }
        else{
            if(nameCategory in categories){
                errorMutableLiveData.value = R.string.modifyCategories__NewCategoryIncluded__error
            }else {
                viewModelScope.launch() {
                    if(myDictionary.isSessionActive()){
                        val db = Firebase.firestore
                        categories.apply { add(nameCategory) }
                        db.collection("categories")
                            .document(myDictionary.getUser())
                            .set(hashMapOf("0" to categories))
                            .addOnSuccessListener{
                                simpleConfirmationMutableLiveData.value = R.string.modifyCategories__ADD__confirmation
                            }.addOnFailureListener {
                                categories.remove(nameCategory)
                                showDialog_DataBaseError(context, builder)
                            }
                    }else{
                        myDictionary.addCategory(nameCategory)
                        categories.add(nameCategory)
                        simpleConfirmationMutableLiveData.value = R.string.modifyCategories__ADD__confirmation
                    }
                }
            }
        }
    }

    fun RENAME_Operation(currentCategory: String, newName: String, context: Context, builder: AlertDialog.Builder?){
        if(currentCategory.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyCategory__error
        }
        else if(newName.isEmpty()){
            errorMutableLiveData.value = R.string.modifyCategories__emptyRename__error
        }
        else if(newName == currentCategory){
            errorMutableLiveData.value = R.string.modifyCategories__RenameToSameCategory__error
        }
        else if(newName in categories){
            errorMutableLiveData.value = R.string.modifyCategories__NewCategoryIncluded__error
        }else{
            viewModelScope.launch() {
                if(myDictionary.isSessionActive()){
                    val db = Firebase.firestore
                    val index = categories.indexOf(currentCategory)
                    categories[index] = newName
                    db.collection("categories")
                        .document(myDictionary.getUser())
                        .set(hashMapOf("0" to categories))
                        .addOnSuccessListener{
                            simpleConfirmationMutableLiveData.value = R.string.modifyCategories__RENAME__confirmation
                        }.addOnFailureListener{
                            categories[index] = currentCategory
                            showDialog_DataBaseError(context, builder)
                        }
                } else{
                    val index = categories.indexOf(currentCategory)
                    myDictionary.renameCategory(currentCategory, newName)
                    categories[index] = newName
                    simpleConfirmationMutableLiveData.value = R.string.modifyCategories__RENAME__confirmation
                }
            }
        }
    }

    fun checkFields_for_ERASE(spinner: Spinner){
        if(spinner.selectedItem == null){
            errorMutableLiveData.value = R.string.modifyCategories__emptyCategory__error
        } else{
            confirmationERASEMutableLiveData.value = true
        }
    }

    fun DELETE_operation(selectedCategory: String, context: Context, builder: AlertDialog.Builder?){
        viewModelScope.launch() {
            if(myDictionary.isSessionActive()){
                val db = Firebase.firestore
                categories.remove(selectedCategory)
                db.collection("categories")
                    .document(myDictionary.getUser())
                    .set(hashMapOf("0" to categories))
                    .addOnFailureListener{
                        categories.add(selectedCategory)
                        showDialog_DataBaseError(context, builder)
                    }
            }else{
                myDictionary.deleteCategory(selectedCategory)
            }
        }
    }
}