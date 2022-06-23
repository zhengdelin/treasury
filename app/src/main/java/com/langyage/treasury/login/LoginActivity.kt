package com.langyage.treasury.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat
import com.langyage.treasury.component.InputComponent
import com.langyage.treasury.MainActivity
import com.langyage.treasury.ToastController
import com.example.treasury.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    private lateinit var loginBtn: Button
    private lateinit var loginEmail: InputComponent
    private lateinit var loginCode: InputComponent

    private var step = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loginBtn = binding.loginBtn.getButton()
        loginEmail = binding.loginEmail
        loginCode = binding.loginCode

        val loginController = LoginController(this, this)
        val toastController = ToastController(this)
        if(loginController.isLogin())
            toMainActivity()

        loginBtn.setOnClickListener {
            val data = getLoginData()
            when(step){
                1 ->{
                    if(data.email.isBlank()){
                        toastController.makeToast("請輸入Email")
                    }else{
                        loginController.login(data, step) {
                            binding.loginCode.layoutParams = LinearLayoutCompat.LayoutParams(
                                LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                            )
                            loginBtn.text = "登入"
                            step++
                        }
                    }
                }
                2 -> {
                    if(data.email.isBlank() || data.code.isNullOrBlank()){
                        toastController.makeToast("請輸入完整資訊")
                    }else{
                        loginController.login(data, step) {
                            toMainActivity()
                        }
                    }
                }
            }
        }
    }
    private fun getLoginData(): LoginData {
        return LoginData(loginEmail.getInputData(), loginCode.getInputData())
    }
    private fun toMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}