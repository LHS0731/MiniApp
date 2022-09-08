package com.cookandorid.miniapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.room.Room
import com.cookandorid.miniapp.model.History

class Cal_Activity : AppCompatActivity() {
    private val expressionTextView: TextView by lazy {
        findViewById(R.id.expressionTextView)
    }
    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private val historyLayout: View by lazy{
        findViewById(R.id.historyLayout)
    }
    private val historyLinearLayout: LinearLayout by lazy{
        findViewById(R.id.historyLinearLayout)
    }

    lateinit var db: CalDatabase


    private var isOperator = false
    private var hasOperator = false

    // 토스트 중복방지 코드
    private var toast: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal)

        db = Room.databaseBuilder(
            applicationContext,
            CalDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun showToast(string: String){
        try{
            toast?.cancel()
            toast = Toast.makeText(this, string, Toast.LENGTH_SHORT)
            toast?.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    //==================

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.btn_0 -> numberButtonClicked("0")
            R.id.btn_1 -> numberButtonClicked("1")
            R.id.btn_2 -> numberButtonClicked("2")
            R.id.btn_3 -> numberButtonClicked("3")
            R.id.btn_4 -> numberButtonClicked("4")
            R.id.btn_5 -> numberButtonClicked("5")
            R.id.btn_6 -> numberButtonClicked("6")
            R.id.btn_7 -> numberButtonClicked("7")
            R.id.btn_8 -> numberButtonClicked("8")
            R.id.btn_9 -> numberButtonClicked("9")
            R.id.btn_add -> operatorButtonClicked("+")
            R.id.btn_sub -> operatorButtonClicked("-")
            R.id.btn_mul -> operatorButtonClicked("*")
            R.id.btn_div -> operatorButtonClicked("/")
            R.id.btn_modulo -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String) {

        if(isOperator){
            expressionTextView.append(" ")
        }
        isOperator = false

        val expressionText: List<String> = expressionTextView.text.split(" ")
        if(expressionText.isNotEmpty() && expressionText.last().length>=15){
            Toast.makeText(this, "15자리 까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            return
        } else if(expressionText.last().isEmpty() && number == "0"){
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        expressionTextView.append(number)
        Log.d("what", expressionText.toString())
        //TODO resultTextView 실시간으로 계산 결과를 넣어야 하는 기능

        resultTextView.text = calculateExpression()

    }

    @SuppressLint("NewApi")
    private fun operatorButtonClicked(operator: String) {
        if (expressionTextView.text.isEmpty()){
            return
        }
        when{
            isOperator ->{
                val text = expressionTextView.text.toString()
                //맨끝 한자리지우고 연산자 더해줌
                expressionTextView.text = text.dropLast(1)+operator

            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용 가능합니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else ->{
                //숫자만 입력하고 연산자가 한번도 들어오지 않은경우
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTextView.text.length -1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        expressionTextView.text = ssb
        isOperator = true
        hasOperator = true
    }

    fun resultButtonClicked(v: View) {
        val expressionTexts = expressionTextView.text.split(" ")
        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1){
            return
        }
        if(expressionTexts.size != 3 && hasOperator){
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
        }
        if (expressionTexts[0].isNumber().not()||expressionTexts[2].isNumber().not()){
            Toast.makeText(this, "오류가 발생했습니다..", Toast.LENGTH_SHORT).show()
            return
        }
        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()


        //DB에 넣어주는 부분
        Thread(Runnable {
            db.historyDao().insertHistory(History(null,expressionText,resultText))
        }).start()

        expressionTextView.text = resultText
        resultTextView.text = ""
        isOperator=false
        hasOperator=false
    }
    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")
        if (hasOperator.not() || expressionTexts.size !=3){
            return ""
        } else if (expressionTexts[0].isNumber().not()||expressionTexts[2].isNumber().not()){
            return ""
        }
        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when(op){
            "+" ->(exp1 + exp2).toString()
            "-" ->(exp1 - exp2).toString()
            "*" ->(exp1 * exp2).toString()
            "/" ->(exp1 / exp2).toString()
            "%" ->(exp1 % exp2).toString()
            else ->""
        }
    }


    fun clearButtonClicked(v: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator=false
        hasOperator=false

    }

    fun historyButtonClicked(v: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {

                runOnUiThread{
                    val historyView = LayoutInflater.from(this).inflate(R.layout.cal_history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)
                }
            }
            //최신것은 나중에저장되는데 이를 먼저 보이게 하기 위해 reversed이용
            //하나씩 each 꺼내와서 listlayout에 넣어줌줌
        }).start()

   }
    fun closeHistoryButtonClicked(v: View){
        historyLayout.isVisible = false

    }
    fun historyClearButtonClicked(v: View){
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
        //TODO DB 모든기록 삭제
        //TODO 뷰에서 모든 기록 삭제`
    }
}

fun String.isNumber(): Boolean{
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException){
        false
    }
}


