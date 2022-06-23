package com.langyage.treasury.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SPController(context: Context) {
    private var sharedPreferences:SharedPreferences
    private val edit get() = sharedPreferences.edit()
    init {
        this.sharedPreferences = context.getSharedPreferences("data", 0)
    }
    fun setData(vararg data: SPData){
        for (item in data){
            Log.i("setData", "key:${item.key}, value:${item.value}")
            when(item.value){
                is String -> {
                    edit.putString(item.key, item.value).apply()
                }
                is Int -> {
                    edit.putInt(item.key, item.value).apply()
                }
                is Float -> {
                    edit.putFloat(item.key, item.value).apply()
                }
                is Boolean -> {
                    edit.putBoolean(item.key, item.value).apply()
                }
            }
        }
    }

    fun getData(key:String, type:Any): SPData {
        return when(type){
            is String -> {
                SPData(key, sharedPreferences.getString(key,""))
            }
            is Int -> {
                SPData(key, sharedPreferences.getInt(key, 0))
            }
            is Float -> {
                SPData(key, sharedPreferences.getFloat(key, 0f))
            }
            is Boolean -> {
                SPData(key, sharedPreferences.getBoolean(key, false))
            }
            else -> SPData(key, null)
        }
    }

    fun clear(){
        edit.clear().apply()
    }
}