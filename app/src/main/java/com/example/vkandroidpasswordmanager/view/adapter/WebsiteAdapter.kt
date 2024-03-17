package com.example.vkandroidpasswordmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vkandroidpasswordmanager.databinding.ItemWebsiteBinding
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.utils.ViewExtensions.loadFavicon

interface WebsiteInteractionListener {
    fun onClicked(website: Website)
}

class WebsiteAdapter(
    private val onInteractionListener: WebsiteInteractionListener
) : ListAdapter<Website, WebsiteAdapter.WebsiteViewHolder>(
    WebsiteCallback()
) {

    class WebsiteViewHolder(
        private val binding: ItemWebsiteBinding,
        private val onInteractionListener: WebsiteInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(website: Website) {
            with(binding) {
                faviconImageView.loadFavicon(
                    if (website.url.endsWith("/")) "${website.url}favicon.ico"
                    else "${website.url}/favicon.ico"
                )
                nameTextView.text = website.name.ifBlank { website.url }
                root.setOnClickListener {
                    onInteractionListener.onClicked(website)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder {
        val binding = ItemWebsiteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WebsiteViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        val website = currentList[position]
        holder.bind(website)
    }
}

class WebsiteCallback : DiffUtil.ItemCallback<Website>() {

    override fun areItemsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem == newItem
    }
}