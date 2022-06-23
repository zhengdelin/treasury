package com.langyage.treasury

import android.content.Context
import android.widget.Toast

class ToastController(context:Context) {
    private var context:Context
    init {
        this.context = context
    }
    fun makeToast(text:String, toastLong:Boolean = false){
        Toast.makeText(context, text, if(toastLong)Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}