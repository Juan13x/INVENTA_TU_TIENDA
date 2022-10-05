package com.juanguicj.inventa_tu_tienda.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDictionary.create() //just for testing
        //myCloudDataBase.create() //just for testing
        val builder: AlertDialog.Builder = this.let{
            AlertDialog.Builder(it)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        mainViewModel.isSessionActive_Coroutine_LiveData.observe(this@MainActivity){
                isSessionActive->
            if (isSessionActive) {
                val user = auth.currentUser
                if(user != null){
                    mainViewModel.setSimpleLogin()
                }else{
                    mainViewModel.clearLoginMain()
                    Toast.makeText(this@MainActivity, R.string.database__expiredSession__error, Toast.LENGTH_LONG).show()
                }
            }else{
                mainViewModel.getCategoriesFromDictionary()
                mainViewModel.setCategoryChange()
            }
        }

        mainViewModel.logInMainLiveData.observe(this@MainActivity){
                logIn->
            if (logIn) {
                tabs.setScrollPosition(1,0f, true)
                viewPager.currentItem = 1
                mainViewModel.getCategoriesFromDataBase(this@MainActivity, builder)
            }else{
                mainViewModel.getCategoriesFromDictionary()
            }
        }
    }

    override fun onStart(){
        super.onStart()
        mainViewModel.isSessionActive()
    }
}