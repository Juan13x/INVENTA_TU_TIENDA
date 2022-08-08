package com.juanguicj.inventa_tu_tienda.fragments.modify.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.juanguicj.inventa_tu_tienda.databinding.FragmentModifyMainBinding

class ModifyMainFragment : Fragment() {
    private lateinit var binding : FragmentModifyMainBinding
    private lateinit var viewModel: ModifyMainViewModel

    companion object {
        fun newInstance() = ModifyMainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModifyMainBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[ModifyMainViewModel::class.java]

        return view
    }
}