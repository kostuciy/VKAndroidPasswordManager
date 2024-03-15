package com.example.vkandroidpasswordmanager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.FragmentWebsiteBinding
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.viewmodel.WebsiteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebsiteFragment : Fragment() {
    private val viewModel: WebsiteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentWebsiteBinding.inflate(inflater, container, false)

        val website = viewModel.currentWebsite

        // val adapter = TODO()

        with(binding) {
            if (website == Website()) return@with
            titleEditText.setText(website.name)
            urlEditText.setText(website.url)
            with(passwordsRecyclerView) {
                //this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }
        }



        return binding.root
    }

}