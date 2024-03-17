package com.example.vkandroidpasswordmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vkandroidpasswordmanager.databinding.ItemPasswordBinding
import com.example.vkandroidpasswordmanager.model.dto.Password

interface PasswordInteractionListener {
    fun onCopyClick(password: Password)
    fun onClick(password: Password) // TODO: redo as long press
}

class PasswordAdapter(
    private val onInteractionListener: PasswordInteractionListener
) : ListAdapter<Password, PasswordAdapter.PasswordViewHolder>(
    PasswordCallback()
) {

    class PasswordViewHolder(
        private val binding: ItemPasswordBinding,
        private val onInteractionListener: PasswordInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(password: Password) {
            with(binding) {
                contextTextView.text = password.context
                passwordTextView.text = password.password // do encryption
                copyButton.setOnClickListener {
                    onInteractionListener.onCopyClick(password)
                }
                root.setOnClickListener {
                    onInteractionListener.onClick(password)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding = ItemPasswordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PasswordViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password = currentList[position]
        holder.bind(password)
    }
}

class PasswordCallback : DiffUtil.ItemCallback<Password>() {

    override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
        return oldItem == newItem
    }
}