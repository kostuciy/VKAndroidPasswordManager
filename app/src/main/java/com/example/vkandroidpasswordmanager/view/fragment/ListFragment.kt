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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vkandroidpasswordmanager.R
import com.example.vkandroidpasswordmanager.databinding.FragmentListBinding
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.view.adapter.WebsiteInteractionListener
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

        val adapter = WebsiteAdapter(object : WebsiteInteractionListener {
            override fun onClicked(website: Website) {
                viewModel.select(website)
                findNavController().navigate(
                    R.id.action_listFragment_to_websiteFragment
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
                val website = adapter.currentList[position]
                viewModel.delete(website.id)
            }
        })

        with(binding) {
            with(toolbar) {
                inflateMenu(R.menu.menu_list)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.password_button -> {
                            findNavController().navigate(R.id.action_listFragment_to_passwordDialog)
                            true
                        }
                        else -> false
                    }
                }
            }

            websiteRecyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
                itemTouchHelper.attachToRecyclerView(this)
            }

            floatingActionButton.setOnClickListener {
                viewModel.selectToEmpty()
                findNavController().navigate(
                    R.id.action_listFragment_to_websiteFragment
                )
            }
        }

        with(viewModel) {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    list.collect { adapter.submitList(it.map { it.website }) }
                }
            }
        }

        return binding.root
    }
}