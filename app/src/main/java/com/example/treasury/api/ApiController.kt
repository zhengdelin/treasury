package com.example.treasury.api

import android.content.Context
import android.util.Log
import com.example.treasury.cloud.DeleteData
import com.example.treasury.cloud.NextIdData
import com.example.treasury.cloud.UploadData
import com.example.treasury.login.LoginController
import com.example.treasury.login.LoginData
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class ApiController(context:Context?=null) {
    private var okHttpClient:OkHttpClient = OkHttpClient().newBuilder().build()
    private var verify:String = ""

    init {
        if(context!=null)
            this.verify = LoginController(context).getVerify()
    }

    fun get(data: ApiData, callBack:Callback){
        val request:Request = Request.Builder().url(data.url).get().build()
        val call = okHttpClient.newCall(request)
        call.enqueue(callBack)
    }

    fun post(data: ApiData, callBack: Callback){
        data.requestBody?.let{
            val request:Request =  Request.Builder().url(data.url).post(it).build()
            val call = okHttpClient.newCall(request)
            call.enqueue(callBack)
        }
    }
    fun handleResponse(
        response:Response,
        success:(JSONObject)->Unit,
        failure:()->Unit
    ){
        val res = response.body?.string()
        Log.i("login res:",res!!)
        if(!res.isNullOrEmpty()){
            val jsonObject = JSONObject(res)
            val code:Int = jsonObject.getInt("code")
            if(code == 200){
                success(jsonObject)
            }else
                failure()
        }
    }
    private fun jsonToRequestBody(jsonObject: JSONObject): RequestBody {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        Log.i("jsonToRequestBody:",jsonObject.toString() )
        return jsonObject.toString().toRequestBody(mediaType)
    }
    fun dataClassToRequestBody(data: LoginData): RequestBody {
        val jsonObject = JSONObject()
        jsonObject.put("email",data.email)
        if(!data.code.isNullOrEmpty())
            jsonObject.put("code",data.code)
        return jsonToRequestBody(jsonObject)
    }
    fun dataClassToRequestBody(data: NextIdData): RequestBody {
        val jsonObject = JSONObject()
        jsonObject.put("fop",data.fop)
        jsonObject.put("verify",verify)
        return jsonToRequestBody(jsonObject)
    }
    fun dataClassToRequestBody(data: UploadData): RequestBody {
        val jsonObject = JSONObject()
        jsonObject.put("verify",verify)
        jsonObject.put("folders", data.folders)
        jsonObject.put("passwords", data.passwords)
        return jsonToRequestBody(jsonObject)
    }fun dataClassToRequestBody(data: DeleteData): RequestBody {
        val jsonObject = JSONObject()
        jsonObject.put("verify",verify)
        jsonObject.put("id", data.id)
        jsonObject.put("fop", data.fop)
        return jsonToRequestBody(jsonObject)
    }

    fun getVerifyRequestBody():RequestBody{
        val jsonObject = JSONObject()
        jsonObject.put("verify",verify)
        return jsonToRequestBody(jsonObject)
    }

}