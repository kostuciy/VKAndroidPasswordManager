package com.example.vkandroidpasswordmanager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.FragmentLoginBinding
import com.example.vkandroidpasswordmanager.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        val biometricPrompt = BiometricPrompt(
            requireActivity(),
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        with(binding) {
            with(setupPasswordButton) {
                isVisible = true // TODO: show is master-password is not set
                setupPasswordButton.setOnClickListener {
                    findNavController().navigate(R.id.action_authFragment_to_passwordDialog)
                }
            }
            with(loginBiometryButton) {
                isVisible = viewModel.biometryAvailability
                setOnClickListener {
                    if (!viewModel.checkBiometryCompatibility()) return@setOnClickListener
                    viewModel.authenticate(biometricPrompt)
                }
            }


        }

        with(viewModel) {

        }






        return binding.root

    }

}