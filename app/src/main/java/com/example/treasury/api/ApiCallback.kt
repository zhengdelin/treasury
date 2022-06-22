package com.example.treasury.api

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

interface ApiCallback: Callback {
    override fun onFailure(call: Call, e: IOException) {
//        Toast.makeText(context, "出現問題 請稍後再試",Toast.LENGTH_SHORT).show()
        Log.i("ApiCallback onFailure","出現問題")
        e.printStackTrace()
    }

    override fun onResponse(call: Call, response: Response)

}