package com.example.proect23.ui.indicator_values

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.IndicatorValue
import com.example.proect23.databinding.ItemIndicatorValueBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class IndicatorValuesAdapter(
    private var list: List<IndicatorValue> = emptyList()
) : RecyclerView.Adapter<IndicatorValuesAdapter.VH>() {

    inner class VH(private val b: ItemIndicatorValueBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: IndicatorValue) {
            val dateFormatted = try {
                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                formatter.format(parser.parse(item.valueDate) ?: item.valueDate)
            } catch (e: Exception) {
                item.valueDate
            }

            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
                maximumFractionDigits = 2
            }

            val originalText = "Дата: $dateFormatted\n" +
                    "Значение: ${numberFormat.format(item.value)} ${item.currencyCode}"
            b.tvValueInfo.text = originalText

            val converted = item.convertedValue
            b.tvConverted.text = if (converted != null) {
                "→ ${numberFormat.format(converted)}"
            } else {
                "→ —"
            }

            b.tvWarning.isVisible = item.warning != null
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
