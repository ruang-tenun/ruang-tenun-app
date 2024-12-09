package com.ruangtenun.app.viewmodel.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.data.remote.response.Product
import com.ruangtenun.app.databinding.CardProductBinding

class AdapterProduct(private val onItemClick: ((String?) -> Unit)? = null) :
    ListAdapter<Product, AdapterProduct.FavoriteEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding =
            CardProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteEventViewHolder(
        private val binding: CardProductBinding,
        private val onItemClick: ((String?) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productCategories.text = product.categories
            Glide.with(binding.productPhoto.context)
                .load(product.imageUrl)
                .into(binding.productPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(product.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Product> =
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(
                    oldItem: Product,
                    newItem: Product
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Product,
                    newItem: Product
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}