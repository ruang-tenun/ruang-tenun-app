package com.ruangtenun.app.viewmodel.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.R
import com.ruangtenun.app.data.remote.response.FavoriteItem
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.databinding.CardProductBinding

class AdapterProduct(
    private val onItemClick: ((Int?) -> Unit)? = null
) : ListAdapter<ProductsItem, AdapterProduct.AdapterProductViewHolder>(DIFF_CALLBACK) {

    private var favoriteList: List<FavoriteItem> = emptyList()

    fun setFavorites(favorites: List<FavoriteItem>) {
        this.favoriteList = favorites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterProductViewHolder {
        val binding =
            CardProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterProductViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AdapterProductViewHolder, position: Int) {
        val product = getItem(position)
        val isFavorite = favoriteList.any { it.productId == product.productId }
        holder.bind(product, isFavorite)
    }

    class AdapterProductViewHolder(
        private val binding: CardProductBinding,
        private val onItemClick: ((Int?) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsItem, isFavorite: Boolean) {
            binding.productName.text = product.name
            binding.productSeller.text = product.seller
            Glide.with(binding.productPhoto.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .into(binding.productPhoto)

            binding.fabLove.setImageResource(
                if (isFavorite) R.drawable.favorite_24dp_filled else R.drawable.favorite_24dp
            )

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
