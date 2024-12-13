package com.ruangtenun.app.viewmodel.history

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.CardHistoryBinding

class AdapterHistory(
    private val onDeleteClick: ((String?) -> Unit)? = null,
    private val application: Application
) : ListAdapter<ClassificationHistory, AdapterHistory.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, onDeleteClick, application)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: CardHistoryBinding,
        private val onDeleteClick: ((String?) -> Unit)?,
        private val application: Application
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: ClassificationHistory) {
            binding.apply {
                catalogName.text = history.weavingName
                val confidenceLevel = when {
                    history.confidenceScore!! < 0.3 -> application.getString(R.string.low)
                    history.confidenceScore!! in 0.3..0.6 -> application.getString(R.string.enough)
                    history.confidenceScore!! in 0.6..0.85 -> application.getString(R.string.good)
                    else -> application.getString(R.string.very_good)
                }
                catalogArea.text = confidenceLevel
                Glide.with(catalogPhoto.context)
                    .load(history.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(catalogPhoto)

                btnDeleteHistory.setOnClickListener {
                    onDeleteClick?.invoke(history.id)
                }
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
