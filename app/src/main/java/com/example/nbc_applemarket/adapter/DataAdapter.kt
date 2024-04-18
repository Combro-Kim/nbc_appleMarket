package com.example.nbc_applemarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nbc_applemarket.data.Data
import com.example.nbc_applemarket.databinding.ItemDummyBinding
import java.text.DecimalFormat

class DataAdapter(private val data: MutableList<Data>,private val isLiked:Boolean) : RecyclerView.Adapter<DataAdapter.Holder>() {
//    var isHeart = false

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    //길게 눌렀을 경우
    interface ItemLongClick {
        fun onLongClick(view: View, position: Int)
    }

    var itemLongClick: ItemLongClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDummyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        holder.itemView.setOnLongClickListener {
            itemLongClick?.onLongClick(it, position)
            true
        }

        holder.bind(data[position])

        if(isLiked){
            holder.itemView
        }
    }


    inner class Holder(private val binding: ItemDummyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Data) {
            binding.apply {
                ivMainImage.setImageResource(data.image)
                tvItemTitle.text = data.title
                tvItemLocation.text = data.location
                val decimal = DecimalFormat("#,###")
                tvItemPrice.text = decimal.format(data.price)
                tvItemLikeNum.text = data.likeNum.toString()
                tvItemChatNum.text = data.chatNum.toString()
/*                if(isLiked){
                    tvItemLikeNum.text = data.likeNum.plus(1).toString()
                }else{
                    tvItemLikeNum.text = data.likeNum.toString()
                }*/
            }
        }
    }
}