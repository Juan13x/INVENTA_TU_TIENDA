package com.juanguicj.inventa_tu_tienda.fragments.user.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentSignUpBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel
import java.util.*
import kotlin.concurrent.timerTask

class SignUpFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUPViewModel: SignUpViewModel
    private val timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)

        signUPViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        with(binding){
            signUpSignUpButton.setOnClickListener {
                signUpPasswordTextInputLayout.error = null
                signUpRepeatPasswordTextInputLayout.error = null
                val user: String = signUpUserEditText.text.toString()
                val password: String = signUpPasswordTextInputEditText.text.toString()
                val repPassword: String = signUpRepeatPasswordTextInputEditText.text.toString()

                signUPViewModel.checkSignUp(user, password, repPassword)
            }

            signUPViewModel.warningUserLiveData.observe(this@SignUpFragment){
                warningEmpty(signUpUserEditText)
            }

            signUPViewModel.warningPasswordLiveData.observe(this@SignUpFragment){
                signUpPasswordTextInputLayout.error = getString(R.string.signUp__emptyPassword)
            }

            signUPViewModel.warningRepeatPasswordLiveData.observe(this@SignUpFragment){
                signUpPasswordTextInputLayout.error = getString(R.string.signUp__emptyPassword)
            }

            signUPViewModel.successSignUpLiveData.observe(this@SignUpFragment){
                mainViewModel.setSignUp(signUpUserEditText.text.toString(),
                    signUpPasswordTextInputEditText.text.toString())
                signUpUserEditText.setText("")
                signUpPasswordTextInputEditText.setText("")
                signUpRepeatPasswordTextInputEditText.setText("")
            }

            signUPViewModel.warningWrongLiveData.observe(this@SignUpFragment){
                    warningWrong->
                val textForToast = getString(warningWrong)
                val toast = Toast.makeText(activity, textForToast,
                    Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    }

    private fun warningEmpty(view: EditText){
        view.setHintTextColor(ContextCompat.getColor(requireActivity().applicationContext,
            R.color.Red))
        timer.schedule(
            timerTask{
                view.setHintTextColor(ContextCompat.getColor(requireActivity().applicationContext,
                    R.color.Dark_Orange)) },
            1000
        )
    }
}