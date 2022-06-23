package com.langyage.treasury

import android.app.Application

class GlobalVariable:Application() {
    companion object {
        private var apiUrl:String = "https://langyage.in/treasury/"
        fun getApiUrl():String = apiUrl

        fun getNextIdUrl():String = apiUrl +"next.php"
        fun getDeleteUrl():String = apiUrl + "delete.php"
    }
}