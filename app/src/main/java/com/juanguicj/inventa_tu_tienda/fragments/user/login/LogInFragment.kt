package com.juanguicj.inventa_tu_tienda.fragments.user.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentLogInBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel

class LogInFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var logInViewModel: LogInViewModel
    private lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        val view = binding.root
        logInViewModel = ViewModelProvider(this)[LogInViewModel::class.java]

        with(binding){
            logInViewModel.warningLiveData.observe(viewLifecycleOwner){
                    warning->
                warningTextView.text = getString(warning)
            }

            logInViewModel.successLogInLiveData.observe(viewLifecycleOwner){
                mainViewModel.setLogin(
                    logInUserEditText.text.toString()
                )
                val text: Editable = SpannableStringBuilder("")
                logInUserEditText.text = text
                loginPasswordTextInputEditText.text = text
                warningTextView.text = ""
            }

            logInLogInButton.setOnClickListener {
                logInViewModel.checkLogIn(logInUserEditText.text.toString(),
                    loginPasswordTextInputEditText.text.toString())
            }
        }

        return view
    }

}