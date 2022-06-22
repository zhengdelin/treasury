package com.example.treasury.passwords

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.treasury.GlobalVariable
import com.example.treasury.MyDBHelper
import com.example.treasury.cloud.NextIdData
import com.example.treasury.R
import com.example.treasury.ToastController
import com.example.treasury.api.ApiCallback
import com.example.treasury.api.ApiController
import com.example.treasury.api.ApiData
import com.example.treasury.cloud.DeleteData
import com.example.treasury.login.LoginController
import okhttp3.Call
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class PasswordController(private var context: Context, private var activity: Activity?= null) {
    private var db:SQLiteDatabase = MyDBHelper(context).writableDatabase
    private var args:Array<String> = context.resources.getStringArray(R.array.password_args)
    private var apiController:ApiController = ApiController(context)
    private var toastController:ToastController = ToastController(context)

    fun addPassword(folder_id:Int, data: PasswordData, success:()->Unit){
        val apiData = apiController.dataClassToRequestBody(NextIdData("p"))
        apiController.post(
            ApiData(GlobalVariable.getNextIdUrl(), apiData),
            object:ApiCallback{
                override fun onResponse(call: Call, response: Response) {
                    apiController.handleResponse(response, {
                        val id = it.getInt("id")
                        handleAddPassword(
                            PasswordData(data.title, data.password, data.note, id, folder_id)
                        )
                        activity?.runOnUiThread {
                            toastController.makeToast("成功新增密碼")
                            success()
                        }

                    },{
                        activity?.runOnUiThread {
                            toastController.makeToast("新增失敗")
                        }

                    })
                }
            }
        )
    }
    fun handleAddPassword(passwordData: PasswordData){
        val cv = ContentValues()
        cv.put("id", passwordData.id)
        cv.put("title", passwordData.title)
        cv.put("password",passwordData.password)
        cv.put("note", passwordData.note)
        cv.put("folder_id", passwordData.folder_id)
        db.insert("passwords", null, cv)
    }
    fun getPasswords(folder_id: Int):ArrayList<PasswordData>{
        val cursor:Cursor = db.rawQuery("SELECT * from passwords WHERE folder_id = ${folder_id}", null)
        val data = ArrayList<PasswordData>()
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val note = cursor.getString(cursor.getColumnIndexOrThrow("note"))

                data.add(PasswordData(title, password, note, id, folder_id))
            }while (cursor.moveToNext())
        }

        return data;
    }

    fun getAllPasswords():ArrayList<PasswordData>{
        val cursor:Cursor = db.rawQuery("SELECT * from passwords", null)
        val data = ArrayList<PasswordData>()
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val note = cursor.getString(cursor.getColumnIndexOrThrow("note"))
                val folderId = cursor.getInt(cursor.getColumnIndexOrThrow("folder_id"))

                data.add(PasswordData(title, password, note, id, folderId))
            }while (cursor.moveToNext())
        }
        return data;
    }

    fun updatePassword(passwordData: PasswordData){
        val cv = ContentValues()
        cv.put("title", passwordData.title)
        cv.put("password",passwordData.password)
        cv.put("note", passwordData.note)
        db.update("passwords",cv,"id=?", arrayOf(passwordData.id.toString()))
        toastController.makeToast("更新密碼成功")
    }

    fun deletePassword(
        passwordData: PasswordData,
        deleteWithCloud:Boolean,
        success: () -> Unit
    ){
        Log.i("deletePassword:", "${passwordData.id}, ${deleteWithCloud}")
        if(deleteWithCloud){
            val requestBody = apiController.dataClassToRequestBody(DeleteData("p", passwordData.id))
            with(apiController){
                post(ApiData(GlobalVariable.getDeleteUrl(), requestBody), object:ApiCallback{
                    override fun onResponse(call: Call, response: Response) {
                        handleResponse(response,{
                            activity?.runOnUiThread {
                                handleDeletePassword(passwordData)
                                success()
                            }
                        },{})
                    }
                })
            }
        }else{
            handleDeletePassword(passwordData)
            success()
        }

    }
    private fun handleDeletePassword(passwordData: PasswordData){
        Log.i("handleDeletePassword:", "${passwordData.id}, ${passwordData.title}")

        db.delete("passwords", "id=?", arrayOf(passwordData.id.toString()))
        toastController.makeToast("密碼已刪除")
    }


    fun passwordDataToBundle(passwordData: PasswordData):Bundle{
        return bundleOf(
            args[0] to passwordData.id,
            args[1] to passwordData.title,
            args[2] to passwordData.password,
            args[3] to passwordData.note)
    }

    fun bundleToPasswordData(bundle:Bundle):PasswordData{
        val passwordData = PasswordData("","")
        bundle.let {
            passwordData.id = it.getInt(args[0])
            passwordData.title = it.getString(args[1]).toString()
            passwordData.password = it.getString(args[2]).toString()
            passwordData.note = it.getString(args[3]).toString()
        }
        return passwordData
    }

    fun getJsonObject(data: PasswordData): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("id", data.id)
        jsonObject.put("title", data.title)
        jsonObject.put("password", data.password)
        jsonObject.put("note", data.note)
        jsonObject.put("folder_id", data.folder_id)
        return jsonObject
    }
    fun getJsonArray(data:ArrayList<PasswordData>): JSONArray {
        val jsonArray = JSONArray()
        //將所有folder轉換
        for (i in data){
            val jsonObject = getJsonObject(i)
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }

    fun getPasswordData(item:JSONObject):PasswordData{
        return PasswordData(
            item.getString("title"),
            item.getString("password"),
            item.getString("note"),
            item.getInt("id"),
            item.getInt("folder_id")
        )
    }
}