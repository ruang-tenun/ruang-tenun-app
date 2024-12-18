package com.ruangtenun.app.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "classification_history")
@Parcelize
data class ClassificationHistory(
    @PrimaryKey
    @ColumnInfo(name = "classification_id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "weaving_name")
    var weavingName: String? = null,
    @ColumnInfo(name = "confidence_score")
    var confidenceScore: Double? = null,
    @ColumnInfo(name = "image_url")
    var imageUrl: String? = null,
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
) : Parcelable