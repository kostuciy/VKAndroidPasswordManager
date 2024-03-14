package com.example.vkandroidpasswordmanager.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.PasswordDialogBinding

//@AndroidEntryPoint
class PasswordDialog : DialogFragment() {
//    lateinit var viewModel: AuthViewModel by activityViewModels
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
//                    TODO: update view model

                    dismiss()
                }.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, id ->
                    dismiss()
                }.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}