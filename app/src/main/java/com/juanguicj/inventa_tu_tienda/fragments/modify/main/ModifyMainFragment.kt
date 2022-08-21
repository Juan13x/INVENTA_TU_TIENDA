package com.juanguicj.inventa_tu_tienda.fragments.modify.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.juanguicj.inventa_tu_tienda.databinding.FragmentModifyMainBinding

class ModifyMainFragment : Fragment() {
    private lateinit var binding : FragmentModifyMainBinding
    private val modifyMainViewModel: ModifyMainViewModel by activityViewModels()

    companion object {
        fun newInstance() = ModifyMainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModifyMainBinding.inflate(inflater, container, false)

        with(binding){
            modifyMainWarningTextView.visibility = View.INVISIBLE
            modifyMainProductsFragmentContainerView.visibility = View.GONE
            modifyMainCategoriesFragmentContainerView.visibility = View.GONE

            modifyMainProductsButton.setOnClickListener {
                if(modifyMainProductsFragmentContainerView.visibility == View.VISIBLE){
                    modifyMainProductsFragmentContainerView.visibility = View.GONE
                }
                else{
                    modifyMainProductsFragmentContainerView.visibility = View.VISIBLE
                }
            }
            modifyMainCategoriesButton.setOnClickListener {
                if(modifyMainCategoriesFragmentContainerView.visibility == View.VISIBLE){
                    modifyMainCategoriesFragmentContainerView.visibility = View.GONE
                }
                else{
                    modifyMainCategoriesFragmentContainerView.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }
}