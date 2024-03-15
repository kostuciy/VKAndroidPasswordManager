package com.example.vkandroidpasswordmanager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.FragmentListBinding
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.view.adapter.OnInteractionListener
import com.example.vkandroidpasswordmanager.view.adapter.WebsiteAdapter
import com.example.vkandroidpasswordmanager.viewmodel.WebsiteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val viewModel: WebsiteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)

        val adapter = WebsiteAdapter(object : OnInteractionListener {
            override fun onClicked(website: Website) {
//                viewModel.toCurrentProduct(product)
//                findNavController().navigate(R.id.action_listFragment_to_productFragment)
                TODO()
            }
        })

        with(binding) {
            websiteRecyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }

            floatingActionButton.setOnClickListener {
//                TODO: redo
                val website = Website(
                    url = "https://vk.com",
                    name = "VK"
                )

                viewModel.select(website)
                viewModel.save()
            }
        }

        with(viewModel) {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    list.collect { adapter.submitList(it) }
                }
            }
        }

        return binding.root
    }
}