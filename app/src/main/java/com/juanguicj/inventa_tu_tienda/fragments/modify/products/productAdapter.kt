package com.juanguicj.inventa_tu_tienda.fragments.modify.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.juanguicj.inventa_tu_tienda.R

const val codePosition = 0
const val namePosition = 1
const val pricePosition = 2
const val amountPosition = 3

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var myData = arrayOf<Array<String?>>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.findViewById(R.id.products_code_textView)
        val name: TextView = view.findViewById(R.id.products_name_textView)
        val price: TextView = view.findViewById(R.id.products_price_textView)
        val amount: TextView = view.findViewById(R.id.products_amount_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.code.text = myData[position][codePosition].toString()
        Log.i("code", myData[position][codePosition].toString())

        holder.name.text = myData[position][namePosition].toString()
        Log.i("name", myData[position][namePosition].toString())

        holder.price.text = myData[position][pricePosition].toString()
        Log.i("price", myData[position][pricePosition].toString())

        holder.amount.text = myData[position][amountPosition].toString()
        Log.i("amount", myData[position][amountPosition].toString())
    }

    fun deleteAll(){
        myData = arrayOf<Array<String?>>()
        notifyDataSetChanged()
    }
    fun updateAll(array: Array<Array<String?>>){
        myData = array
        notifyDataSetChanged()
    }

//    fun addData(newData: ArrayList<String>): Boolean{
//        if(newData.size == 4){
//            myData.add(arrayListOf(newData[0],newData[1],newData[2],newData[3]))
//            return true
//        }
//        return false
//    }

    override fun getItemCount(): Int {
        return myData.size
    }
}