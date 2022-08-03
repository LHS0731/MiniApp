package com.cookandorid.miniapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class BMI_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val height : EditText = findViewById(R.id.height)
        val weight : EditText = findViewById(R.id.weight)
        val button : Button = findViewById(R.id.result_btn)
        button.setOnClickListener {
            if (height.text.isEmpty()) {
                Toast.makeText(this, "신장 데이터가 비어 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (weight.text.isEmpty()) {
                Toast.makeText(this, "체중 데이터가 비어 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val height_int: Int = height.text.toString().toInt()
            val weight_int: Int = weight.text.toString().toInt()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("height", height_int)
            intent.putExtra("weight", weight_int)

            startActivity(intent)
        }
    }
}