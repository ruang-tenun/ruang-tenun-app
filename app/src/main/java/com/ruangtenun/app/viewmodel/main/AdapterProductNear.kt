package com.ruangtenun.app.viewmodel.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.databinding.CardProductNearBinding

class AdapterProductNear(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<ProductsItem, AdapterProductNear.ProductNearViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductNearViewHolder {
        val binding =
            CardProductNearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductNearViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ProductNearViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductNearViewHolder(
        private val binding: CardProductNearBinding,
        private val onItemClick: ((Int?) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsItem) {
            binding.productName.text = product.name
            binding.productArea.text = product.address
            binding.sellerName.text = product.seller
            binding.productCategories.text = product.category
            Glide.with(binding.productPhoto.context)
                .load(product.imageUrl)
                .into(binding.productPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(product.productId)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ProductsItem> =
            object : DiffUtil.ItemCallback<ProductsItem>() {
                override fun areItemsTheSame(
                    oldItem: ProductsItem,
                    newItem: ProductsItem
                ): Boolean {
                    return oldItem.productId == newItem.productId
                }

                override fun areContentsTheSame(
                    oldItem: ProductsItem,
                    newItem: ProductsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}