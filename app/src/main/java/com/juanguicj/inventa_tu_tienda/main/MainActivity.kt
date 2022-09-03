package com.juanguicj.inventa_tu_tienda.main

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.juanguicj.inventa_tu_tienda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDictionary.create()
        myDataBase.create()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        with(binding){
            mainViewModel.logInMainLiveData.observe(this@MainActivity){
                    logIn->
                if (logIn) {
                    tabs.setScrollPosition(1,0f, true)
                    viewPager.currentItem = 1
                    mainViewModel.linkFunctionGetUpdatedCategoriesToDataBase()
                } else {
                    mainViewModel.linkFunctionGetUpdatedCategoriesToDictionary()
                }
            }
        }
    }
}