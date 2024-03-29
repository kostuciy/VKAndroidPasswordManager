package com.example.vkandroidpasswordmanager.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.DialogSetupBinding
import com.example.vkandroidpasswordmanager.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupDialog : DialogFragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = DialogSetupBinding.inflate(
                layoutInflater, null, false
            )

            with(binding) {
                showPasswordButton.setOnClickListener {
                    passwordEditText.transformationMethod =
                        if (!showPasswordButton.isChecked)
                            PasswordTransformationMethod.getInstance()
                        else HideReturnsTransformationMethod.getInstance()
                }
            }

            AlertDialog.Builder(it)
                .setMessage(getString(R.string.dialog_title))
                .setView(binding.root)
                .setPositiveButton(getString(R.string.dialog_submit)) { dialog, id ->
                    val password = binding.passwordEditText.text.toString()
                    val confirmation = binding.passwordConfirmEditText.text.toString()
                    if (password != confirmation) {
                        Toast.makeText(context,
                            getString(R.string.toast_passwords_not_match), Toast.LENGTH_SHORT)
                            .show()
                        return@setPositiveButton
                    }

                    findNavController().navigateUp()
                    viewModel.setMasterPassword(password)
                }.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, id ->
                    findNavController().navigateUp()
                }.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}