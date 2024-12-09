package com.ruangtenun.app.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {
    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String? = null,
        onPositiveClick: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, which ->
                dialog.dismiss()
                onPositiveClick?.invoke()
            }
            .setNegativeButton(negativeButtonText) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

}