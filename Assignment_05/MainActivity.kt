package com.example.basiccalculator   // 🔴 change if different

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val et1 = findViewById<EditText>(R.id.etNumber1)
        val et2 = findViewById<EditText>(R.id.etNumber2)
        val resultText = findViewById<TextView>(R.id.tvResult)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            calculate(et1, et2, resultText, "+")
        }

        findViewById<Button>(R.id.btnSub).setOnClickListener {
            calculate(et1, et2, resultText, "-")
        }

        findViewById<Button>(R.id.btnMul).setOnClickListener {
            calculate(et1, et2, resultText, "*")
        }

        findViewById<Button>(R.id.btnDiv).setOnClickListener {
            calculate(et1, et2, resultText, "/")
        }
    }

    private fun calculate(
        et1: EditText,
        et2: EditText,
        resultText: TextView,
        operator: String
    ) {
        val num1 = et1.text.toString().toDoubleOrNull()
        val num2 = et2.text.toString().toDoubleOrNull()

        if (num1 == null || num2 == null) {
            resultText.text = "Result: Invalid Input"
            return
        }

        val result = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> if (num2 != 0.0) num1 / num2 else {
                resultText.text = "Result: Cannot divide by zero"
                return
            }
            else -> 0.0
        }

        resultText.text = "Result: $result"
    }
}