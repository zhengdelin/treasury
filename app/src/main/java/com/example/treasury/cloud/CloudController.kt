package com.example.treasury.cloud

import android.app.Activity
import android.content.Context
import androidx.navigation.findNavController
import com.example.treasury.GlobalVariable
import com.example.treasury.ToastController
import com.example.treasury.api.ApiCallback
import com.example.treasury.api.ApiController
import com.example.treasury.api.ApiData
import com.example.treasury.folders.FolderController
import com.example.treasury.folders.FolderData
import com.example.treasury.login.LoginActivity
import com.example.treasury.login.LoginController
import com.example.treasury.passwords.PasswordController
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject

class CloudController(private val context: Context, private val activity: Activity) {
    private val folderController = FolderController(context)
    private val passwordController = PasswordController(context)
    private val apiController = ApiController(context)
    private val toastController = ToastController(context)

    fun upload(){
        val folders = folderController.getJsonArray(folderController.getFolders())
        val passwords = passwordController.getJsonArray(passwordController.getAllPasswords())
        val requestBody = apiController.dataClassToRequestBody(
            UploadData(folders, passwords)
        )
        val url = GlobalVariable.getApiUrl()+"upload.php"
        apiController.post(ApiData(url, requestBody),object:ApiCallback{
            override fun onResponse(call: Call, response: Response) {
                apiController.handleResponse(response,{
                    activity.runOnUiThread {
                        toastController.makeToast("上傳成功")
                    }
                },{
                    activity.runOnUiThread {
                        toastController.makeToast("上傳失敗，請稍後再試")
                    }
                })
            }
        } )
    }

    fun download(success:()->Unit){
        val requestBody = apiController.getVerifyRequestBody()
        val url = GlobalVariable.getApiUrl() + "download.php"

        with(apiController) {

            post(ApiData(url, requestBody), object : ApiCallback {
                override fun onResponse(call: Call, response: Response) {
                    handleResponse(response,{

                        val folders = it.getJSONArray("folders")
                        val _folders = folderController.getFolders()
                        //插入資料夾
                        var len = folders.length()
                        for (i in 0 until len -1 ){
                            val item = folderController.getFolderData(folders.getJSONObject(i))
                            if(!_folders.contains(item)){
                                folderController.handleAddFolder(item)
                            }
                        }
                        
                        //插入密碼
                        val passwords = it.getJSONArray("passwords")
                        val _passwrods = passwordController.getAllPasswords()
                        len = passwords.length()
                        for (i in 0 until len-1){
                            val item = passwordController.getPasswordData(passwords.getJSONObject(i))
                            if(!_passwrods.contains(item)){
                                passwordController.handleAddPassword(item)
                            }
                        }

                        activity.runOnUiThread {
                            success()
                        }

                    },{
                        activity.runOnUiThread {
                            toastController.makeToast("下載失敗，請稍後再試")
                        }
                    })
                }

            })
        }
    }

}