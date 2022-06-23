package com.langyage.treasury.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.langyage.treasury.GlobalVariable
import com.langyage.treasury.ToastController
import com.langyage.treasury.api.ApiCallback
import com.langyage.treasury.api.ApiController
import com.langyage.treasury.api.ApiData
import com.langyage.treasury.shared_preferences.SPController
import com.langyage.treasury.shared_preferences.SPData
import okhttp3.Call
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject

class LoginController(private val context: Context, private val activity: Activity?=null) {
    private val url = GlobalVariable.getApiUrl()+"login.php"
    private val apiController = ApiController()
    private var spController: SPController = SPController(context)
    private val toastController = ToastController(context)
    fun getVerify():String
         = spController.getData("verify","String").value.toString()

    fun isLogin():Boolean
        = !getVerify().isEmpty()

    fun login(loginData: LoginData, step:Int, success:()-> Unit ){
        val data :RequestBody = apiController.dataClassToRequestBody(loginData)
        when(step){
            1->{
                //寄送驗證信
                apiController.post(ApiData(url, data),object : ApiCallback {
                    override fun onResponse(call: Call, response: Response) {
                        val res = response.body?.string()
                        Log.i("login step1 res:",res!!)
                        if(!res.isNullOrEmpty()){
                            val jsonObject = JSONObject(res)
                            val code:Int = jsonObject.getInt("code")
                            if(code == 200){
                                activity?.runOnUiThread {
                                    toastController.makeToast("寄送驗證信成功")
                                    success()
                                }
                            }
                        }
                    }
                })
            }
            2->{
                //登入
                apiController.post(ApiData(url, data),object : ApiCallback {
                    override fun onResponse(call: Call, response: Response) {
                        val res = response.body?.string()
                        Log.i("login step2 res:",res!!)
                        if(!res.isNullOrEmpty()){
                            val jsonObject = JSONObject(res)
                            val code:Int = jsonObject.getInt("code")
                            if(code == 200){
                                val verify = jsonObject.getString("verify")
                                spController.setData(
                                    SPData("verify", verify), SPData("email",loginData.email)
                                )
                                activity?.runOnUiThread {
                                    toastController.makeToast("登入成功")
                                    success()
                                }
                            }else activity?.runOnUiThread {
                                    val msg = jsonObject.getString("msg")
                                    toastController.makeToast(msg)
                            }

                        }
                    }
                })
            }
        }
    }

    fun logout(){
        SPController(context).clear()
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}