package com.juanguicj.inventa_tu_tienda.fragments.user.changepassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.juanguicj.inventa_tu_tienda.R
import com.juanguicj.inventa_tu_tienda.databinding.FragmentChangePasswordBinding
import com.juanguicj.inventa_tu_tienda.main.MainViewModel

class ChangePasswordFragment : Fragment() {
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        changePasswordViewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]

        with(binding){
            changePasswordChangePasswordButton.setOnClickListener {
                changePasswordCurrentPasswordTextInputLayout.error = null
                changePasswordNewPasswordTextInputLayout.error = null
                changePasswordRepeatNewPasswordTextInputLayout.error = null
                val currentPassword: String = changePasswordCurrentPasswordTextInputEditText.text.toString()
                val newPassword: String = changePasswordNewPasswordTextInputEditText.text.toString()
                val repNewPassword: String = changePasswordRepeatNewPasswordTextInputEditText.text.toString()

                changePasswordViewModel.checkChange(currentPassword, newPassword, repNewPassword)
            }

            changePasswordViewModel.warningCurrentPasswordLiveData.observe(viewLifecycleOwner){
                changePasswordCurrentPasswordTextInputLayout.error = getString(R.string.changePassword__emptyField)
            }

            changePasswordViewModel.warningNewPasswordLiveData.observe(viewLifecycleOwner){
                changePasswordNewPasswordTextInputLayout.error = getString(R.string.changePassword__emptyField)
            }

            changePasswordViewModel.warningRepeatNewPasswordLiveData.observe(viewLifecycleOwner){
                changePasswordRepeatNewPasswordTextInputLayout.error = getString(R.string.changePassword__emptyField)
            }

            changePasswordViewModel.successChangePasswordLiveData.observe(viewLifecycleOwner){
                mainViewModel.setNewPassword()
                val text: Editable = SpannableStringBuilder("")
                changePasswordCurrentPasswordTextInputEditText.text = text
                changePasswordNewPasswordTextInputEditText.text = text
                changePasswordRepeatNewPasswordTextInputEditText.text = text
                val toast = Toast.makeText(activity, getString(R.string.changePassword__succesChangePassword), Toast
                    .LENGTH_SHORT)
                toast.show()
            }

            changePasswordViewModel.warningWrongLiveData.observe(viewLifecycleOwner){
                    wrongOption->
                val textForToast: String = getString(wrongOption)
                val toast = Toast.makeText(activity, textForToast, Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        return binding.root
    }
}