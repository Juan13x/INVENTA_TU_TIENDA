package com.juanguicj.inventa_tu_tienda.fragments.modify.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentModifyCategoryBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel

class ModifyCategoryFragment : Fragment() {
    private lateinit var binding: FragmentModifyCategoryBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var modifyCategoryViewModel: ModifyCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentModifyCategoryBinding.inflate(layoutInflater)
        modifyCategoryViewModel = ViewModelProvider(this)[ModifyCategoryViewModel::class.java]

        with(binding){

        }
    }

}