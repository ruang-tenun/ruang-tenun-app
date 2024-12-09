package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("result")
    var result: String? = null
)
