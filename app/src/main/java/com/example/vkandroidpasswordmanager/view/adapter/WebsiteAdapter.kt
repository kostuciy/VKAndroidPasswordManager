package com.example.vkandroidpasswordmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vkandroidpasswordmanager.databinding.WebsiteItemBinding
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.utils.ViewExtensions.loadFavicon

interface OnInteractionListener {
    fun onClicked(website: Website)
}

class WebsiteAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Website, WebsiteAdapter.ProductViewHolder>(
    ProductCallback()
) {

    class ProductViewHolder(
        private val binding: WebsiteItemBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(website: Website) {
            with(binding) {
                faviconImageView.loadFavicon("${website.url}/favicon.ico")
                nameTextView.text = website.name
                root.setOnClickListener {
                    onInteractionListener.onClicked(website)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = WebsiteItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = currentList[position]
        holder.bind(product)
    }
}

class ProductCallback : DiffUtil.ItemCallback<Website>() {

    override fun areItemsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem == newItem
    }
}