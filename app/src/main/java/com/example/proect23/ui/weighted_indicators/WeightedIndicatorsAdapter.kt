package com.example.proect23.ui.weighted_indicators

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.WeightedIndicator
import com.example.proect23.databinding.ItemWeightedIndicatorBinding

class WeightedIndicatorsAdapter(
    private var list: List<WeightedIndicator> = emptyList()
) : RecyclerView.Adapter<WeightedIndicatorsAdapter.VH>() {

    inner class VH(val b: ItemWeightedIndicatorBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: WeightedIndicator) {
            b.tvName.text       = item.indicatorName
            b.tvWeighted.text   = "Σ: ${item.weightedValue}"
            b.tvConverted.text  = "→ ${item.convertedWeightedValue ?: "—"} ${item.currencyCode}"
            b.tvWarning.text    = item.warning ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemWeightedIndicatorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submit(newList: List<WeightedIndicator>) {
        list = newList
        notifyDataSetChanged()
    }
}
