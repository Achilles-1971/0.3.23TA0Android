package com.example.proect23.ui.indicators

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.Indicator
import com.example.proect23.databinding.ItemIndicatorBinding

class IndicatorsAdapter(
    private var list: List<Indicator> = emptyList(),
    private val onClick: (Indicator) -> Unit
) : RecyclerView.Adapter<IndicatorsAdapter.VH>() {

    inner class VH(private val binding: ItemIndicatorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Indicator) {
            binding.tvIndicatorName.text = item.name
            binding.tvIndicatorImportance.text = "Важность: ${item.importance} (${item.unit})"

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemIndicatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submit(newList: List<Indicator>) {
        list = newList
        notifyDataSetChanged()
    }
}
