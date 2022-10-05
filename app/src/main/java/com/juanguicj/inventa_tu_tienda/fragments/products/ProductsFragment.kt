package com.juanguicj.inventa_tu_tienda.fragments.products

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentProductsBinding
import com.juanguicj.inventa_tu_tienda.fragments.modify.products.ProductAdapter
import com.juanguicj.inventa_tu_tienda.main.MainViewModel
import com.juanguicj.inventa_tu_tienda.main.myDictionary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductsFragment()
    }

    private lateinit var binding: FragmentProductsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var productsViewModel: ProductsViewModel
    private val productAdapter = ProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        productsViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        val myContext = this@ProductsFragment.requireContext()
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        with(binding){
            productsProductsReciclerView.adapter = productAdapter

            with(productsViewModel){
                productsCategoriesListView.onItemClickListener =
                    OnItemClickListener { adapterView, view, position, id ->
                        val category = adapterView.getItemAtPosition(position)
                        checkCategoryField(category)
                    }

                afterSelectionConfirmationLiveData.observe(viewLifecycleOwner){
                    afterCategorySelected(myContext, builder)
                }
                errorSystemLiveData.observe(viewLifecycleOwner){
                    Toast.makeText(requireContext().applicationContext, R.string.products__error__categoryErrorType, Toast.LENGTH_LONG).show()
                }
                errorCategoryLiveData.observe(viewLifecycleOwner){
                    productsCategoryInfoTextView.text = getString(R.string.products__error__category)
                    hideCategoryList()
                    hideProductList()
                }
                errorProductLiveData.observe(viewLifecycleOwner){
                    Toast.makeText(requireContext().applicationContext, R.string.products__error__products, Toast.LENGTH_LONG).show()
                    productsProductsInfoTextView.text = getString(R.string.products__error__products)
                    hideProductList()
                }

                mainViewModel.productChangeLiveData.observe(viewLifecycleOwner){
                    hideProductList()
                    productsProductsInfoTextView.text = ""
                    productAdapter.deleteAll()
                }
                mainViewModel.categoryChangeLiveData.observe(viewLifecycleOwner){
                    updateViewWhenCategoryChange()
                }
            }
        }

        return binding.root
    }

    private fun FragmentProductsBinding.afterCategorySelected(myContext: Context, builder: AlertDialog.Builder?) {
        productsProductsInfoTextView.visibility = View.VISIBLE
        runBlocking{
            launch{
                productsProductsInfoTextView.text = getString(R.string.products__text__productInfo, myDictionary.getCategory())
                productsProductsReciclerView.visibility = View.VISIBLE
                with(productsViewModel){
                    getProductList(productAdapter, myContext, builder)
                }
            }
        }

    }

    private fun FragmentProductsBinding.updateViewWhenCategoryChange() {
        productsCategoryInfoTextView.text = getString(R.string.products__text__categoryInfo)
        hideProductList()
        updateCategoryList()
        productsProductsInfoTextView.text = ""
        productAdapter.deleteAll()
    }

    private fun FragmentProductsBinding.hideCategoryList() {
        null
    }

    private fun FragmentProductsBinding.hideProductList() {
        productsProductsReciclerView.visibility = View.INVISIBLE
    }

    private fun FragmentProductsBinding.updateCategoryList() {
        with(productsViewModel){
            getCategoryList(productsCategoriesListView, this@ProductsFragment.requireContext())
        }
    }
}