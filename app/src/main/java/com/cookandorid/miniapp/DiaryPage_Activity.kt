package com.cookandorid.miniapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryPage_Activity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val clearButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.clearButton)
    }
    private val backButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.backButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_page)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))


        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
        }
        diaryEditText.addTextChangedListener{
            //내용이 수정될때마다 저장되게 하는 기능(텍스트 변경시마다 리스너 호출)
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }

        clearButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("주의!!")
                .setMessage("삭제한 내용은 돌아오지 않습니다. 삭제할까요?")
                .setPositiveButton("확인") { _, _ ->
                    diaryEditText.setText("")
                }
                .setNegativeButton("취소") { _, _ ->

                }
                .create()
                .show()
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, Diary_Activity::class.java))
        }
    }
}