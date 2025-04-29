package com.example.proect23.ui.enterprises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.Enterprise
import com.example.proect23.databinding.ItemEnterpriseBinding

class EnterprisesAdapter(
    private val onClick: (Enterprise) -> Unit
) : ListAdapter<Enterprise, EnterprisesAdapter.VH>(EnterpriseDiffCallback()) {

    private var fullList: List<Enterprise> = emptyList()

    inner class VH(val binding: ItemEnterpriseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Enterprise) = binding.apply {
            tvName.text = item.name
            tvPhone.text = item.phone
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemEnterpriseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    fun submitListWithFull(newList: List<Enterprise>) {
        fullList = newList
        submitList(newList)
    }

    fun filter(query: String) {
        val filtered = if (query.isBlank()) {
            fullList
        } else {
            fullList.filter { it.name.contains(query, ignoreCase = true) }
        }
        submitList(filtered)
    }

    class EnterpriseDiffCallback : DiffUtil.ItemCallback<Enterprise>() {
        override fun areItemsTheSame(oldItem: Enterprise, newItem: Enterprise): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Enterprise, newItem: Enterprise): Boolean =
            oldItem == newItem
    }
}
