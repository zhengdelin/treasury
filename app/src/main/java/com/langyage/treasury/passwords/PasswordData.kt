package com.langyage.treasury.passwords

data class PasswordData(
    var title:String,
    var password:String,
    var note:String = "",
    var id:Int = 0,
    var folder_id:Int = 0)
