package com.ruangtenun.app.viewmodel.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.R
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.databinding.CardCatalogBinding

class AdapterCatalog(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<CatalogItem, AdapterCatalog.CatalogViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding =
            CardCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CatalogViewHolder(
        private val binding: CardCatalogBinding,
        private val onItemClick: ((Int?) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(catalog: CatalogItem) {
            binding.catalogName.text = catalog.name
            binding.catalogArea.text = catalog.address
            Glide.with(binding.catalogPhoto.context)
                .load(catalog.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .thumbnail(0.25f)
                .override(400, 300)
                .into(binding.catalogPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(catalog.categoryId)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<CatalogItem> =
            object : DiffUtil.ItemCallback<CatalogItem>() {
                override fun areItemsTheSame(
                    oldItem: CatalogItem,
                    newItem: CatalogItem
                ): Boolean {
                    return oldItem.categoryId == newItem.categoryId
                }

                override fun areContentsTheSame(
                    oldItem: CatalogItem,
                    newItem: CatalogItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}