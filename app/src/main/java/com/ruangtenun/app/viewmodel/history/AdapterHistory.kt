package com.ruangtenun.app.viewmodel.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.data.remote.response.Catalog
import com.ruangtenun.app.databinding.CardCatalogBinding

class AdapterHistory(private val onItemClick: ((String?) -> Unit)? = null) :
    ListAdapter<ClassificationHistory, AdapterHistory.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            CardCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: CardCatalogBinding,
        private val onItemClick: ((String?) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: ClassificationHistory) {
            binding.catalogName.text = history.weavingName
            binding.catalogArea.text = history.confidenceScore.toString()
            Glide.with(binding.catalogPhoto.context)
                .load(history.imageUrl)
                .into(binding.catalogPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(history.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ClassificationHistory> =
            object : DiffUtil.ItemCallback<ClassificationHistory>() {
                override fun areItemsTheSame(
                    oldItem: ClassificationHistory,
                    newItem: ClassificationHistory
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ClassificationHistory,
                    newItem: ClassificationHistory
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}