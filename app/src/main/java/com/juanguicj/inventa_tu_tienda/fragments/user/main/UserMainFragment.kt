package com.juanguicj.inventa_tu_tienda.fragments.user.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentUsermainBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel
import com.juanguicj.inventa_tu_tienda.main.myDictionary

/**
 * A placeholder fragment containing a simple view.
 */
class UserMainFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentUsermainBinding

    companion object {
        fun newInstance() = UserMainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsermainBinding.inflate(inflater, container, false)
        val view = binding.root


        with(binding){
            exitButton.visibility = View.INVISIBLE
            changePasswordButton.visibility = View.GONE

            logInButton.setOnClickListener {
                Navigation.findNavController(userMainFragmentContainerView).navigate(R.id
                    .logInFragment)
            }
            signUpButton.setOnClickListener {
                Navigation.findNavController(userMainFragmentContainerView).navigate(R.id.signUpFragment)
            }
            changePasswordButton.setOnClickListener {
                Navigation.findNavController(userMainFragmentContainerView).navigate(R.id
                    .changePasswordFragment)
            }
            mainViewModel.logInMainLiveData.observe(viewLifecycleOwner){
                    logIn->
                if(logIn){
                    exitButton.visibility = View.VISIBLE
                    changePasswordButton.visibility = View.VISIBLE
                    userInfoTextView.text = getString(R.string.userMain_userInfo__logIn_text,
                        myDictionary.getUser())
                }else{
                    exitButton.visibility = View.INVISIBLE
                    changePasswordButton.visibility = View.GONE
                    userInfoTextView.text = ""
                }
            }

            exitButton.setOnClickListener {
                mainViewModel.clearLoginMain()
            }
        }

        return view
    }
}