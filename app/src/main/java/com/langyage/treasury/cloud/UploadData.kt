package com.langyage.treasury.cloud

import org.json.JSONArray

data class UploadData(
    val folders:JSONArray,
    val passwords:JSONArray
)