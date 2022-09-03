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
    ): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLogInBinding.inflate(layoutInflater)
        logInViewModel = ViewModelProvider(this)[LogInViewModel::class.java]

        with(binding){
            logInViewModel.warningLiveData.observe(this@LogInFragment){
                    warning->
                warningTextView.text = getString(warning)
            }

            logInViewModel.successLogInLiveData.observe(this@LogInFragment){
                mainViewModel.setLogin(
                    logInUserEditText.text.toString()
                )
                logInUserEditText.setText("")
                loginPasswordTextInputEditText.setText("")
                warningTextView.text = ""
            }

            logInLogInButton.setOnClickListener {
                logInViewModel.checkLogIn(logInUserEditText.text.toString(),
                    loginPasswordTextInputEditText.text.toString())
            }
        }
    }
}