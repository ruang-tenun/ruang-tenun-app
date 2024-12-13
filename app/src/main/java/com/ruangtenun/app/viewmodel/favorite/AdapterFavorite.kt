package com.ruangtenun.app.viewmodel.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ruangtenun.app.data.remote.response.PayloadItem
import com.ruangtenun.app.databinding.CardFavoriteBinding

class AdapterFavorite(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<PayloadItem, AdapterFavorite.FavoriteProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        val binding = CardFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) {
            holder.bind(product)
        }
    }

    inner class FavoriteProductViewHolder(private val binding: CardFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PayloadItem) {
            binding.apply {
                // Set data to views, assuming CardProductBinding contains these views
                productName.text = item.productName // Adjust image loading logic if needed

                root.setOnClickListener {
                    onItemClick?.invoke(item.productId)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PayloadItem> =
            object : DiffUtil.ItemCallback<PayloadItem>() {
                override fun areItemsTheSame(
                    oldItem: PayloadItem,
                    newItem: PayloadItem
                ): Boolean {
                    return oldItem.productId == newItem.productId
                }

                override fun areContentsTheSame(
                    oldItem: PayloadItem,
                    newItem: PayloadItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
