package com.juanguicj.inventa_tu_tienda.fragments.user.changepassword

import android.app.AlertDialog
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
import com.juanguicj.inventa_tu_tienda.main.myDictionary

class ChangePasswordFragment : Fragment() {
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        changePasswordViewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        val myContext = this@ChangePasswordFragment.requireContext()
        val builder = activity?.let{ AlertDialog.Builder(it)}

        with(binding){
            changePasswordChangePasswordButton.setOnClickListener {
                changePasswordCurrentPasswordTextInputLayout.error = null
                changePasswordNewPasswordTextInputLayout.error = null
                changePasswordRepeatNewPasswordTextInputLayout.error = null
                val currentPassword: String = changePasswordCurrentPasswordTextInputEditText.text.toString()
                val newPassword: String = changePasswordNewPasswordTextInputEditText.text.toString()
                val repNewPassword: String = changePasswordRepeatNewPasswordTextInputEditText.text.toString()

                changePasswordViewModel.checkChange(currentPassword, newPassword, repNewPassword, myContext, builder)
            }

            changePasswordViewModel.warningCurrentPasswordLiveData.observe(this@ChangePasswordFragment){
                changePasswordCurrentPasswordTextInputLayout.error = getString(R.string.changePassword__emptyField)
            }

            changePasswordViewModel.warningNewPasswordLiveData.observe(this@ChangePasswordFragment){
                changePasswordNewPasswordTextInputLayout.error = getString(R.string.changePassword__emptyField)
            }

            changePasswordViewModel.warningRepeatNewPasswordLiveData.observe(this@ChangePasswordFragment){
                changePasswordRepeatNewPasswordTextInputLayout.error = getString(R.string.changePassword__emptyField)
            }

            changePasswordViewModel.successChangePasswordLiveData.observe(this@ChangePasswordFragment){
                changePasswordCurrentPasswordTextInputEditText.setText("")
                changePasswordNewPasswordTextInputEditText.setText("")
                changePasswordRepeatNewPasswordTextInputEditText.setText("")

                val toast = Toast.makeText(activity, getString(R.string.changePassword__succesChangePassword), Toast
                    .LENGTH_SHORT)
                toast.show()
            }

            changePasswordViewModel.warningWrongLiveData.observe(this@ChangePasswordFragment){
                    wrongOption->
                val textForToast: String = getString(wrongOption)
                val toast = Toast.makeText(activity, textForToast, Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}