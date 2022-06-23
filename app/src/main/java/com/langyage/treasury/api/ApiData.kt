package com.langyage.treasury.api

import okhttp3.RequestBody

data class ApiData(val url:String, val requestBody:RequestBody? = null){}