package com.example.nbc_applemarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nbc_applemarket.data.Data
import com.example.nbc_applemarket.databinding.ItemDummyBinding
import java.text.DecimalFormat

class DataAdapter(private val data: MutableList<Data>) : RecyclerView.Adapter<DataAdapter.Holder>() {

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDummyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it,position)
        }
        holder.apply {
            image.setImageResource(data[position].image)
            title.text = data[position].title
            location.text = data[position].location
            likeNum.text = data[position].likeNum.toString()
            chatNum.text = data[position].chatNum.toString()

            val decimal = DecimalFormat("#,###")
            price.text = decimal.format(data[position].price.toString().toInt())
        }
    }

    inner class Holder(binding: ItemDummyBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivMainImage
        val title = binding.tvItemTitle
        val location = binding.tvItemLocation
        val likeNum = binding.tvItemLikeNum
        val chatNum = binding.tvItemChatNum
        val price = binding.tvItemPrice
    }
}