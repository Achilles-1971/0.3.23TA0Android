package com.example.proect23.ui.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.Currency
import com.example.proect23.databinding.ItemCurrencyBinding

class CurrenciesAdapter(
    private var list: List<Currency> = emptyList()
) : RecyclerView.Adapter<CurrenciesAdapter.VH>() {

    inner class VH(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Currency) {
            binding.tvCurrencyCode.text = item.code
            binding.tvCurrencyName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCurrencyBinding.inflate(
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

    fun submit(newList: List<Currency>) {
        list = newList
        notifyDataSetChanged()
    }
}
