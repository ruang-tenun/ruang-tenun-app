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
    private val onItemClick: ((Int?) -> Unit)? = null,
    private val onFavoriteClick: ((Int?, Boolean) -> Unit)? = null
) : ListAdapter<ProductsItem, AdapterProduct.AdapterProductViewHolder>(DIFF_CALLBACK) {

    private var favoriteList: List<FavoriteItem> = emptyList()
    private val favoriteSet = mutableSetOf<Int>()

    fun setFavorites(favorites: List<FavoriteItem>) {
        this.favoriteList = favorites
        favoriteSet.clear()
        favoriteSet.addAll(favorites.map { it.productId })
    }

    fun getFavoriteIdByProductId(productId: Int): Int? {
        return favoriteList.find { it.productId == productId }?.favoriteId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterProductViewHolder {
        val binding =
            CardProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterProductViewHolder(binding, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: AdapterProductViewHolder, position: Int) {
        val product = getItem(position)
        val isFavorite = favoriteSet.contains(product.productId)
        holder.bind(product, isFavorite)
    }

    fun updateFavoriteStatus(productId: Int, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteSet.add(productId)
        } else {
            favoriteSet.remove(productId)
        }
    }

    class AdapterProductViewHolder(
        private val binding: CardProductBinding,
        private val onItemClick: ((Int?) -> Unit)?,
        private val onFavoriteClick: ((Int?, Boolean) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsItem, isFavorite: Boolean) {
            binding.apply {
                productName.text = product.name
                productSeller.text = product.seller
                Glide.with(productPhoto.context)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(productPhoto)

                fabLove.setImageResource(
                    if (isFavorite) R.drawable.favorite_24dp_filled else R.drawable.favorite_24dp
                )

                root.setOnClickListener {
                    onItemClick?.invoke(product.productId)
                }

                fabLove.setOnClickListener {
                    val newState = !isFavorite
                    onFavoriteClick?.invoke(product.productId, newState)
                    fabLove.setImageResource(
                        if (newState) R.drawable.favorite_24dp_filled else R.drawable.favorite_24dp
                    )
                }
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

