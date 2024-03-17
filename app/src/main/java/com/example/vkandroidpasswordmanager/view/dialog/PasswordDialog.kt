package com.example.vkandroidpasswordmanager.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.DialogPasswordBinding
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.viewmodel.WebsiteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordDialog : DialogFragment() {
    private val viewModel: WebsiteViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val password =
            if (viewModel.currentPassword.id == 0L)
                viewModel.currentPassword.copy(
                    id = viewModel.currentWebsitePasswords.value.size + 1L
                ) else viewModel.currentPassword

        return activity?.let {
            val binding = DialogPasswordBinding.inflate(
                layoutInflater, null, false
            )

            if (password != Password()) {
                with(binding) {
                    passwordEditText.setText(password.password)
                    contextEditText.setText(password.context)
                }
            }

            AlertDialog.Builder(it)
                .setMessage(getString(R.string.dialog_password_message))
                .setView(binding.root)
                .setPositiveButton(getString(R.string.dialog_submit)) { dialog, id ->
                    val passwordText = binding.passwordEditText.text.toString()
                    val context = binding.contextEditText.text.toString()
                    if (passwordText.isBlank() || context.isBlank()) {
                        Toast.makeText(getContext(),
                            getString(R.string.toast_fields_filled), Toast.LENGTH_SHORT)
                            .show()
                        return@setPositiveButton
                    }


                    viewModel.select(
                        password.copy(
                            context = context,
                            password = passwordText
                        ))
                    viewModel.addPassword()

                    findNavController().navigateUp()
                }.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, id ->
                    findNavController().navigateUp()
                }.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}