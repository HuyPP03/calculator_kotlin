package com.learn.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat
import java.util.Stack

class MainActivity :View.OnClickListener, AppCompatActivity() {

    private lateinit var textReuslt: TextView

    private var str: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textReuslt = findViewById(R.id.text_result)
        findViewById<Button>(R.id.button_add_sub).setOnClickListener(this)
        findViewById<Button>(R.id.button_0).setOnClickListener(this)
        findViewById<Button>(R.id.button_dot).setOnClickListener(this)
        findViewById<Button>(R.id.button_equal).setOnClickListener(this)
        findViewById<Button>(R.id.button_1).setOnClickListener(this)
        findViewById<Button>(R.id.button_2).setOnClickListener(this)
        findViewById<Button>(R.id.button_3).setOnClickListener(this)
        findViewById<Button>(R.id.button_add).setOnClickListener(this)
        findViewById<Button>(R.id.button_4).setOnClickListener(this)
        findViewById<Button>(R.id.button_5).setOnClickListener(this)
        findViewById<Button>(R.id.button_6).setOnClickListener(this)
        findViewById<Button>(R.id.button_sub).setOnClickListener(this)
        findViewById<Button>(R.id.button_7).setOnClickListener(this)
        findViewById<Button>(R.id.button_8).setOnClickListener(this)
        findViewById<Button>(R.id.button_9).setOnClickListener(this)
        findViewById<Button>(R.id.button_mul).setOnClickListener(this)
        findViewById<Button>(R.id.button_ce).setOnClickListener(this)
        findViewById<Button>(R.id.button_c).setOnClickListener(this)
        findViewById<Button>(R.id.button_bs).setOnClickListener(this)
        findViewById<Button>(R.id.button_div).setOnClickListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onClick(view: View?) {
        val id = view?.id;
        when(id){
            R.id.button_0 -> addDigit("0")
            R.id.button_1 -> addDigit("1")
            R.id.button_2 -> addDigit("2")
            R.id.button_3 -> addDigit("3")
            R.id.button_4 -> addDigit("4")
            R.id.button_5 -> addDigit("5")
            R.id.button_6 -> addDigit("6")
            R.id.button_7 -> addDigit("7")
            R.id.button_8 -> addDigit("8")
            R.id.button_9 -> addDigit("9")
            R.id.button_add -> addDigit("+")

            R.id.button_sub -> addDigit("-")

            R.id.button_mul -> addDigit("*")

            R.id.button_div -> addDigit("/")

            R.id.button_dot -> addDigit(".")
            R.id.button_equal -> {
                val decimalFormat = DecimalFormat("#.#####")
                if(str != "Error") str = decimalFormat.format(calculate(str))
                textReuslt.text = str
            }
            R.id.button_add_sub -> {}
            R.id.button_c -> {
                str = "0"
                textReuslt.text = str
            }
            R.id.button_ce -> {}
            R.id.button_bs -> {
                str = if(str.length > 1) {
                    str.substring(0, str.length - 1)
                }else{
                    "0"
                }
                textReuslt.text = str
            }
            else -> {}
        }
    }

    private  fun addDigit(c: String) {
        if(str == "0" || str == "Error") {
            str = c;
        }else{
            str += c;
        }
        textReuslt.text = str;
    }

    private fun calculate(expression: String): Double {
        val numbers = Stack<Double>()
        val operations = Stack<Char>()

        var i = 0
        while (i < expression.length) {
            val char = expression[i]
            if (char == ' ') {
                i++
                continue
            }
            if (i == 0 && isOperator(expression[i])) {
                i++
                var number = 0.0
                while (i < expression.length && expression[i].isDigit()) {
                    number = number * 10 + (expression[i] - '0')
                    i++
                }
                if(char == '-') numbers.push(-number)
                else if(char == '+') numbers.push(number)
                continue
            }
            if (char.isDigit()) {
                var number = 0.0
                while (i < expression.length && expression[i].isDigit()) {
                    number = number * 10 + (expression[i] - '0')
                    i++
                }
                if (i < expression.length && expression[i] == '.') {
                    i++
                    var decimalPlace = 0.1
                    while (i < expression.length && expression[i].isDigit()) {
                        number += (expression[i] - '0') * decimalPlace
                        decimalPlace /= 10
                        i++
                    }
                }
                numbers.push(number)
                continue
            }
            if (isOperator(char)) {
                while (operations.isNotEmpty() && precedence(operations.peek()) >= precedence(char)) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()))
                }
                operations.push(char)
            }
            i++
        }
        while (operations.isNotEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()))
        }

        return numbers.pop()
    }

    private fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == '*' || c == '/'
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> 0
        }
    }

    private fun applyOperation(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else {
                str = "Error"
                return 0.0
            }
            else -> 0.0
        }
    }
}