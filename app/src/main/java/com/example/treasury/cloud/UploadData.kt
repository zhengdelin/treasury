package com.example.treasury.cloud

import com.example.treasury.folders.FolderData
import com.example.treasury.passwords.PasswordData
import org.json.JSONArray

data class UploadData(
    val folders:JSONArray,
    val passwords:JSONArray
)