package com.example.vkandroidpasswordmanager.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.FragmentWebsiteBinding
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.view.adapter.PasswordAdapter
import com.example.vkandroidpasswordmanager.view.adapter.PasswordInteractionListener
import com.example.vkandroidpasswordmanager.viewmodel.WebsiteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WebsiteFragment : Fragment() {
    private val viewModel: WebsiteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWebsiteBinding.inflate(inflater, container, false)

        val website = viewModel.currentData.value.website

        val adapter = PasswordAdapter(object : PasswordInteractionListener {
             override fun onCopyClick(password: Password) {
                 val clipData = ClipData.newPlainText(
                     "password",
                     password.password
                 )
                 getSystemService(requireContext(), ClipboardManager::class.java)
                     ?.setPrimaryClip(clipData)

                 Toast.makeText(context, getString(R.string.toast_copied), Toast.LENGTH_SHORT)
                     .show()
             }

             override fun onClick(password: Password) {
                 viewModel.select(password)
                 findNavController().navigate(
                     R.id.action_websiteFragment_to_passwordDialog2
                 )
             }
         })
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val password = adapter.currentList[position]
                viewModel.removePassword(password.id)
            }
        })

        with(binding) {
            with(passwordsRecyclerView) {
                this.adapter = adapter.apply {
                    submitList(emptyList())
                }
                layoutManager = LinearLayoutManager(context)
                itemTouchHelper.attachToRecyclerView(this)
            }
            with(toolbar) {
                inflateMenu(R.menu.menu_website)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.add_button -> {
                            val url = urlEditText.text.toString()
                            val name = nameEditText.text.toString()

                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                Toast.makeText(
                                    context, context.getString(R.string.error_wrong_url_format),
                                    Toast.LENGTH_SHORT
                                ).show()
                            else {
                                viewModel.select(
                                    website = website.copy(url = url, name = name),
                                    withPasswords = false
                                )
                                viewModel.save()
                                findNavController().navigateUp()
                            }

                            true
                        }
                        else -> false
                    }
                }
            }
            addPasswordButton.setOnClickListener {
                viewModel.selectPasswordToEmpty()
                findNavController().navigate(
                    R.id.action_websiteFragment_to_passwordDialog2
                )
            }

            if (website == Website()) return@with
            nameEditText.setText(website.name)
            urlEditText.setText(website.url)
        }

        with(viewModel) {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    currentData.collect { adapter.submitList(it.passwords) }
                }
            }
        }

        return binding.root
    }
}