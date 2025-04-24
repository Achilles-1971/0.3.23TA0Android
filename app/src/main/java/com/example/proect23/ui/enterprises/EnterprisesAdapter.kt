package com.example.proect23.ui.enterprises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proect23.data.model.Enterprise
import com.example.proect23.databinding.ItemEnterpriseBinding

class EnterprisesAdapter(
    private var list: List<Enterprise> = emptyList()
) : RecyclerView.Adapter<EnterprisesAdapter.VH>() {

    inner class VH(private val vb: ItemEnterpriseBinding) : RecyclerView.ViewHolder(vb.root) {
        fun bind(item: Enterprise) {
            vb.tvName.text = item.name
            vb.tvPhone.text = item.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = ItemEnterpriseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun submit(newList: List<Enterprise>) {
        list = newList
        notifyDataSetChanged()
    }
}
