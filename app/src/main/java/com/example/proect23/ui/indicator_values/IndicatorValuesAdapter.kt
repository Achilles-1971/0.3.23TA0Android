package com.example.proect23.ui.indicator_values

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.IndicatorValue
import com.example.proect23.databinding.ItemIndicatorValueBinding

class IndicatorValuesAdapter(
    private var list: List<IndicatorValue> = emptyList()
) : RecyclerView.Adapter<IndicatorValuesAdapter.VH>() {

    inner class VH(private val b: ItemIndicatorValueBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: IndicatorValue) {
            b.tvValueInfo.text =
                "Ent#${item.enterpriseId}  Ind#${item.indicatorId}  Date:${item.valueDate}  Val:${item.value} ${item.currencyCode}"
            b.tvConverted.text =
                if (item.convertedValue != null)
                    "→ ${item.convertedValue}"
                else
                    "→ —"
            b.tvWarning.text = item.warning ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemIndicatorValueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submit(newList: List<IndicatorValue>) {
        list = newList
        notifyDataSetChanged()
    }
}
