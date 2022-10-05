package com.juanguicj.inventa_tu_tienda.fragments.user.login

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.FirebaseError
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.google.firebase.ktx.Firebase
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentLogInBinding
import com.juanguicj.inventa_tu_tienda.main.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        with(binding){
            logInViewModel.warningLiveData.observe(this@LogInFragment){
                    warning->
                warningTextView.text = getString(warning)
            }

            logInViewModel.successLogInLiveData.observe(this@LogInFragment){
                logInUserTextInputEditText.setText("")
                loginPasswordTextInputEditText.setText("")
                warningTextView.text = ""
                runBlocking {
                    launch {
                        mainViewModel.setLogin(myDictionary.getUser())
                    }
                }

            }

            logInLogInButton.setOnClickListener {
                val user = logInUserTextInputEditText.text.toString()
                val password = loginPasswordTextInputEditText.text.toString()
                logInViewModel.logInOperation(user, password, this@LogInFragment.requireContext(), builder)
            }
        }
    }
}