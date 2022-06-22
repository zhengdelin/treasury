package com.example.treasury

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.treasury.cloud.CloudController
import com.example.treasury.databinding.ActivityMainBinding
import com.example.treasury.folders.FolderListFragment
import com.example.treasury.login.LoginActivity
import com.example.treasury.login.LoginController
import com.google.android.material.navigation.NavigationView
import okhttp3.internal.wait

class MainActivity : AppCompatActivity() {

    val log_tag = "Log:"
    lateinit var appBarConfiguration:AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var drawerLayout:DrawerLayout

    lateinit var manager:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //設置toolbar
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_bars_solid)

        manager = supportFragmentManager

        val cloudController = CloudController(binding.root.context, this)

        //drawer
        drawerLayout = binding.drawerLayout
        val navView:NavigationView = binding.navigationDrawer


        appBarConfiguration = AppBarConfiguration(setOf(R.id.folderListFragment), drawerLayout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //抽屜導航被點擊時的監聽器
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_folders -> {
                    navController.navigate(R.id.folderListFragment)
                }
                R.id.menu_logout ->{
                    AlertDialogController(binding.root.context).showAlertDialog("確定要登出嗎"){
                        LoginController(binding.root.context).logout()
                    }
                }
                R.id.menu_upload -> {
                    cloudController.upload()
                }
                R.id.menu_download -> {
                    navController.navigate(R.id.dataRefreshFragment)
                    cloudController.download(){
                        navController.navigateUp()
                    }
                }
                R.id.menu_delete -> {
                    AlertDialogController(binding.root.context).showAlertDialog("確定要刪除所有本地資料"){

                        var db: SQLiteDatabase = MyDBHelper(this).writableDatabase
                        db.execSQL("DELETE FROM folders;")
                        db.execSQL("DELETE FROM passwords;")
                        navController.navigate(R.id.folderListFragment)
                    }
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        super.onCreateOptionsMenu(menu)
//        Log.i(log_tag, "onCreateOptionsMenu has been called")
//        //生成menu
//        menuInflater.inflate(R.menu.main_context_menu, menu)
//        return true
//    }
    //標題列的menu受到點擊
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.i("onOptionsItemSelected",getSharedPreferences("data",0).getString("verify","").toString() )
//
//        if(item.itemId == android.R.id.home) return false
//        return when(item.itemId){
//            R.id.menu_download ->{
////                ToastController(binding.root.context).makeToast(loginController.getVerify())
//                true
//            }
//            R.id.menu_upload -> {
//                true
//            }
//            else -> false
//        }
//    }
}