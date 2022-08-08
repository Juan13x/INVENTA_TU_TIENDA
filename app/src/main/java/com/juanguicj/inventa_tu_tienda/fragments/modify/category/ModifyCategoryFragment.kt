package com.juanguicj.inventa_tu_tienda.fragments.modify.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.juanguicj.inventa_tu_tienda.R

class ModifyCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = ModifyCategoryFragment()
    }

    private lateinit var viewModel: ModifyCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modify_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ModifyCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}