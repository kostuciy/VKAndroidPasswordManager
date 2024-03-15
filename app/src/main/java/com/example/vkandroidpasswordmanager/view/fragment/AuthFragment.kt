package com.example.vkandroidpasswordmanager.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.FragmentLoginBinding
import com.example.vkandroidpasswordmanager.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        val biometricPrompt = BiometricPrompt(
            requireActivity(),
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    Toast.makeText(
                        context,
                        getString(R.string.authenctication_error),
                        Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    viewModel.toAuthenticated()
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(
                        context,
                        getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT).show()
                }
            })

        with(binding) {
            biometryGroup.isVisible = viewModel.biometryAvailability
            with(setupPasswordButton) {
                isVisible = !viewModel.hasMasterPassword()
                setOnClickListener {
                    findNavController().navigate(R.id.action_authFragment_to_passwordDialog)
                }
            }

            loginBiometryButton.setOnClickListener {
                Log.d("GAG", "${viewModel.checkBiometryCompatibility()}")
                if (!viewModel.checkBiometryCompatibility()) return@setOnClickListener
                viewModel.authenticateBiometry(biometricPrompt)
            }
            loginPasswordButton.setOnClickListener {
                val password = masterPasswordEditText.text.toString()
                val currentPassword = viewModel.getMasterPassword()

                when {
                    !viewModel.hasMasterPassword() -> {
                        findNavController().navigate(R.id.action_authFragment_to_passwordDialog)
                        masterPasswordEditText.clearFocus()
                        masterPasswordEditText.text.clear()
                    }
                    password != currentPassword -> {
                        Toast.makeText(
                            context,
                            getString(R.string.toast_wrong_password),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> viewModel.toAuthenticated()
                }
            }
        }

        with(viewModel) {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    state.collect {
                        if (it.authenticated) {
                            findNavController().navigate(R.id.action_authFragment_to_listFragment)
                            binding.masterPasswordEditText.text.clear()
                        }
                    }
                }
            }
        }

        return binding.root
    }
}