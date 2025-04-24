package com.example.proect23.ui.exchange_rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.ExchangeRate
import com.example.proect23.databinding.ItemExchangeRateBinding

class ExchangeRatesAdapter(
    private var list: List<ExchangeRate> = emptyList()
) : RecyclerView.Adapter<ExchangeRatesAdapter.VH>() {

    inner class VH(val binding: ItemExchangeRateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExchangeRate) {
            binding.tvPair.text = "${item.fromCurrency} → ${item.toCurrency}"
            binding.tvRate.text = "Курс: ${item.rate}"
            binding.tvDate.text = "Дата: ${item.rateDate}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemExchangeRateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submit(newList: List<ExchangeRate>) {
        list = newList
        notifyDataSetChanged()
    }
}