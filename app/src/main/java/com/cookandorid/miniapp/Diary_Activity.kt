package com.cookandorid.miniapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class Diary_Activity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.num1).apply {
            minValue = 0
            maxValue = 9
        }
    }
    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.num2).apply {
            minValue = 0
            maxValue = 9
        }
    }
    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.num3).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }
    private val changeButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changeButton)
    }

    private var mode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {

            if(mode){
                Toast.makeText(this, "비밀번호 변경 중입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                //login success
                startActivity(Intent(this, DiaryPage_Activity::class.java))
            } else {
                //login fail
                showErrorDialog()
            }

            changeButton.setOnClickListener{

                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
                val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

                if(mode){
                    //new password save
                    passwordPreferences.edit(true){
                        putString("password",passwordFromUser)
                    }

                    mode = false
                    changeButton.setBackgroundColor(Color.parseColor("#D4A373"))
                }
                else{
                    //password reset
                    if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                        mode = true
                        Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                        changeButton.setBackgroundColor(Color.RED)
                    } else {
                        //login fail
                        showErrorDialog()
                    }
                }
            }
        }

    }
    private fun showErrorDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ ->
            }
            .create()
            .show()
    }
}