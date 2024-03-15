package com.example.vkandroidpasswordmanager.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.PasswordDialogBinding
import com.example.vkandroidpasswordmanager.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordDialog : DialogFragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = PasswordDialogBinding.inflate(
                layoutInflater, null, false
            )

            AlertDialog.Builder(it)
                .setMessage(getString(R.string.dialog_title))
                .setView(binding.root)
                .setPositiveButton(getString(R.string.dialog_submit)) { dialog, id ->
                    val password = binding.passwordEditText.text.toString()
                    val confirmation = binding.passwordConfirmEditText.text.toString()
                    if (password != confirmation) return@setPositiveButton

                    findNavController().navigateUp()
                    viewModel.setMasterPassword(password)
                }.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, id ->
                    findNavController().navigateUp()
                }.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}