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
    var classificationId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "weaving_name")
    var weavingName: String? = null,
) : Parcelable