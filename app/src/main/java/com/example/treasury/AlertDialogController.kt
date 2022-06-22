package com.example.treasury

import android.app.AlertDialog
import android.content.Context

class AlertDialogController(private var context: Context) {
    private var builder:AlertDialog.Builder
    init {
        this.builder = AlertDialog.Builder(context)
    }
    fun showAlertDialog(
        msg:String,
        confirmText:String = "確認",
        cancelText:String = "取消",
        confirmFun:()->Unit
    ){
        builder.setMessage(msg)
            .setPositiveButton(confirmText){ _, _ ->
                confirmFun()
            }
            .setNegativeButton(cancelText){ dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    fun showSingleCheckBoxDialog(
        title:String,
        checkBoxText:String,
        confirmText: String = "確認",
        cancelText: String = "取消",
        confirmFun: (Boolean) -> Unit
    ){
        val list = listOf(checkBoxText)
        var checked = false
        builder.setTitle(title)
            .setMultiChoiceItems(list.toTypedArray(), BooleanArray(1))
            { _, _, isChecked->
                checked = isChecked
            }
            .setPositiveButton(confirmText){ _, _ ->
                confirmFun(checked)
            }
            .setNegativeButton(cancelText){ dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }
}