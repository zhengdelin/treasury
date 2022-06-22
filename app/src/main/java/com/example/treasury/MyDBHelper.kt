package com.example.treasury

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.treasury.folders.FolderController
import com.example.treasury.folders.FolderData

class MyDBHelper(val context: Context):SQLiteOpenHelper(context, name, null, version) {
    companion object{
        val name="treasury.db"
        val version = 8
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createFolderTable =
            "CREATE TABLE if not exists folders(id integer PRIMARY KEY, name varchar(20) NOT NULL, color varchar(9) NOT NULL);"
        val createPasswordTable =
            "CREATE TABLE if not exists passwords(id integer PRIMARY KEY, title varchar(50) NOT NULL, password varchar(60) NOT NULL, note varchar(100) NOT NULL, folder_id int NOT NULL);"
        db?.execSQL(createFolderTable)
        db?.execSQL(createPasswordTable)

//        Toast.makeText(context, "建立資料庫",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, nVersion: Int) {
        val dropFolderTable = "DROP TAblE IF EXISTS folders;"
        val dropPasswordTable = "DROP TAblE IF EXISTS passwords;"
        db?.execSQL(dropFolderTable)
        db?.execSQL(dropPasswordTable)
        onCreate(db)
//        Toast.makeText(context, "更新",Toast.LENGTH_SHORT).show()
    }


}