package com.langyage.treasury.folders

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import com.langyage.treasury.api.ApiCallback
import com.langyage.treasury.api.ApiController
import com.langyage.treasury.api.ApiData
import com.langyage.treasury.cloud.DeleteData
import com.langyage.treasury.cloud.NextIdData
import com.langyage.treasury.GlobalVariable
import com.langyage.treasury.MyDBHelper
import com.langyage.treasury.R
import com.langyage.treasury.ToastController
import okhttp3.Call
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class FolderController(private var context: Context,private var activity: Activity?=null) {
    private var db:SQLiteDatabase = MyDBHelper(context).writableDatabase
    private val args:Array<String> = context.resources.getStringArray(R.array.folder_args)
    private val toastController = ToastController(context)
    private var apiController: ApiController = ApiController(context)

//    新增資料夾
    fun addFolder(data: FolderData, success:()->Unit){
        val apiData = apiController.dataClassToRequestBody(NextIdData("f"))
        apiController.post(
            ApiData(GlobalVariable.getNextIdUrl(), apiData),object: ApiCallback {
                override fun onResponse(call: Call, response: Response) {
                    apiController.handleResponse(response, {
                        val name = data.name
                        val cursor = db.rawQuery("select * from folders where name = '$name'", null)
                        if(cursor.moveToFirst()){
                            activity?.runOnUiThread {
                                toastController.makeToast("資料夾已經存在")
                            }
                        }else{
                            val id = it.getInt("id")
                            handleAddFolder(FolderData(name, data.color, id))
                            activity?.runOnUiThread {
                                toastController.makeToast("新增資料夾成功")
                                success()
                            }
                        }
                    },{
                        activity?.runOnUiThread {
                            toastController.makeToast("新增失敗")
                        }
                    })
                }

            })
    }
//操作資料庫新增資料夾
    fun handleAddFolder(folderData: FolderData){
        val cv = ContentValues()
        cv.put("id", folderData.id)
        cv.put("name",folderData.name)
        cv.put("color", folderData.color)
        db.insert("folders", null, cv)
    }
//取得所有資料夾
    fun getFolders():ArrayList<FolderData>{
        val cursor:Cursor = db.rawQuery("SELECT * FROM folders",null);

        val data = ArrayList<FolderData>()

        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val color = cursor.getString(cursor.getColumnIndexOrThrow("color"))
//                val name = "123"
//                val color = "#ffffff"
                data.add(FolderData(name, color, id))
            }while (cursor.moveToNext())
        }
        return data;
    }
//更新資料夾
    fun updateFolder(folderData: FolderData):Boolean{
        val id = folderData.id
        val name = folderData.name
        var cursor = db.rawQuery("select * from folders where id = $id",null)
        if(cursor.moveToFirst()){
            val _name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            //如果有更改名字
            if(name != _name){
                cursor = db.rawQuery("select * from folders where name = '$name'", null)
//                如果名字已經存在
                if(cursor.moveToFirst()){
                    Toast.makeText(context, "資料夾已經存在",Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            val cv = ContentValues()
            cv.put("name", folderData.name)
            cv.put("color",folderData.color)
            db.update("folders",cv,"id=?", arrayOf(folderData.id.toString()))
            Toast.makeText(context, "更新資料夾成功", Toast.LENGTH_SHORT).show()
            return true
        }
        Toast.makeText(context, "發生未知錯誤",Toast.LENGTH_SHORT).show()
        return false
    }
//刪除資料夾
    fun deleteFolder(folderData: FolderData, deleteWithCloud:Boolean, success:()->Unit){
        if(deleteWithCloud){
            val requestBody = apiController.dataClassToRequestBody(DeleteData("f", folderData.id))
            with(apiController) {
                post(ApiData(com.langyage.treasury.GlobalVariable.getDeleteUrl(), requestBody), object:
                    ApiCallback {
                    override fun onResponse(call: Call, response: Response) {
                        handleResponse(response, {
                            activity?.runOnUiThread {
                                handleDeleteFolder(folderData)
                                success()
                            }
                        }, {})
                    }
                })
            }
        }else{
            handleDeleteFolder(folderData)
            success()
        }
    }
    private fun handleDeleteFolder(folderData: FolderData){
        db.delete("folders","id=?", arrayOf(folderData.id.toString()))
        Toast.makeText(context, "${folderData.name}已刪除", Toast.LENGTH_SHORT).show()
    }
//將bundle轉為資料夾資料
    fun bundleToFolderData(bundle: Bundle): FolderData {
        val folderData = FolderData("","")
        bundle.let {
            folderData.id = it.getInt(args[0])
            folderData.name = it.getString(args[1]).toString()
            folderData.color = it.getString(args[2]).toString()
        }
        return folderData
    }
//將資料夾資料轉為bundle
    fun folderDataToBundle(folderData: FolderData):Bundle{
        return bundleOf(
            args[0] to folderData.id,
            args[1] to folderData.name,
            args[2] to folderData.color)
    }

    /**
     * 傳入顏色取得資料夾icon
     * @param color
     * @return FolderIcon
     **/
    private fun getFolderIcon(color:String):Drawable{
        val folderIcon:Drawable? = AppCompatResources.getDrawable(context, R.drawable.ic_folder_solid)
        val wrappedFolderIcon = DrawableCompat.wrap(folderIcon!!)
        DrawableCompat.setTint(wrappedFolderIcon, Color.parseColor(color))

        return wrappedFolderIcon
    }

    fun setCurrentFolder(folderData: FolderData, textView:TextView, imageView: ImageView){
        textView.text = folderData.name
        imageView.setImageDrawable(getFolderIcon(folderData.color))
    }

    fun getJsonObject(data: FolderData):JSONObject{
        val jsonObject = JSONObject()
        jsonObject.put("id", data.id)
        jsonObject.put("name", data.name)
        jsonObject.put("color", data.color)
        return jsonObject
    }
    fun getJsonArray(data:ArrayList<FolderData>):JSONArray{
        val jsonArray = JSONArray()
        //將所有folder轉換
        for (i in data){
            val jsonObject = getJsonObject(i)
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }

    fun getFolderData(item:JSONObject): FolderData {
        return FolderData(
            item.getString("name"),
            item.getString("color"),
            item.getInt("id")
        )
    }


}