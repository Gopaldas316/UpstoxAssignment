package com.example.upstoxassignment.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.upstoxassignment.databinding.RowItemHoldingItemBinding
import com.example.upstoxassignment.models.HoldingsRecord

class HoldingsListAdapter(private val listOfHolding: ArrayList<HoldingsRecord>) :
    RecyclerView.Adapter<HoldingsListAdapter.HoldingItemViewholder>() {
    inner class HoldingItemViewholder(val binding: RowItemHoldingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingItemViewholder {
        val binding = RowItemHoldingItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return HoldingItemViewholder(binding)
    }

    override fun getItemCount(): Int {
        return listOfHolding.size
    }

    override fun onBindViewHolder(holder: HoldingItemViewholder, position: Int) {
        val item = listOfHolding[position]

        holder.binding.tvSymbol.text = "Symbol: ${item.symbol}"
        holder.binding.tvNetQty.text = "NET QTY : ${item.netQty}"
        holder.binding.tvPNL.text = "P&L :  ₹" + String.format("%.2f", item.pnl)
        if (item.pnl < 0.0) {
            holder.binding.tvPNL.setTextColor(Color.RED)
        }

        holder.binding.tvLTP.text = "LTP : ₹" + item.ltp
    }

    fun updateList(list: List<HoldingsRecord>) {
        listOfHolding.clear()
        listOfHolding.addAll(list)
        notifyDataSetChanged()
    }
}