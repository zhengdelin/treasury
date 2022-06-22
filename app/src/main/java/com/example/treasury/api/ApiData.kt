package com.example.treasury.api

import com.example.treasury.login.LoginData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class ApiData(val url:String, val requestBody:RequestBody? = null){}