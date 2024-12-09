package com.ruangtenun.app.viewmodel.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.data.remote.response.Catalog
import com.ruangtenun.app.databinding.CardCatalogBinding

class AdapterCatalog(private val onItemClick: ((String?) -> Unit)? = null) :
    ListAdapter<Catalog, AdapterCatalog.CatalogViewHolder>(DIFF_CALLBACK) {

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
        private val onItemClick: ((String?) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(catalog: Catalog) {
            binding.catalogName.text = catalog.name
            binding.catalogArea.text = catalog.local
            Glide.with(binding.catalogPhoto.context)
                .load(catalog.imageUrl)
                .into(binding.catalogPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(catalog.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Catalog> =
            object : DiffUtil.ItemCallback<Catalog>() {
                override fun areItemsTheSame(
                    oldItem: Catalog,
                    newItem: Catalog
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Catalog,
                    newItem: Catalog
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}