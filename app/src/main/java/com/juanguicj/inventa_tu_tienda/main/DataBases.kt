package com.juanguicj.inventa_tu_tienda.main

import android.R
import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

val auth: FirebaseAuth = Firebase.auth
val categories: ArrayList<String> = ArrayList()

val myDictionary = PersistentClass()
//val myCloudDataBase = CloudDataBase()

data class ProductsType(val name: String, val amount: Int, val price: Float)

//--------------------------------------
//Dialog
//--------------------------------------
fun showDialog_DataBaseError(context: Context, builder: AlertDialog.Builder?){
    builder?.setMessage(com.juanguicj.inventa_tu_tienda.R.string.database_unknownAndUnIdentifiedError)
        ?.setPositiveButton("Ok"){_,_->}
    builder?.create()?.show()
}

//SPINNER
fun setSpinnerEntries(spinner: Spinner, context: Context, viewModel: MainViewModel) {
    val changedSpinnerList = categories
    val adapter: ArrayAdapter<String> = ArrayAdapter(
        context,
        R.layout.simple_spinner_item,
        changedSpinnerList
    )
    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter
}