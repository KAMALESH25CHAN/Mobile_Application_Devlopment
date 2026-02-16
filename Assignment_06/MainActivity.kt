package com.example.scientific_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.tvDisplay)
        val grid = findViewById<GridLayout>(R.id.GridLayout)

        for (i in 0 until grid.childCount) {
            val button = grid.getChildAt(i) as Button
            button.setOnClickListener {
                handleClick(button.text.toString())
            }
        }
    }

    private fun handleClick(value: String) {
        when (value) {
            "=" -> calculateResult()
            "C" -> {
                currentInput = ""
                display.text = "0"
            }
            "sin" -> applyFunction { sin(Math.toRadians(it)) }
            "cos" -> applyFunction { cos(Math.toRadians(it)) }
            "tan" -> applyFunction { tan(Math.toRadians(it)) }
            "log" -> applyFunction { log10(it) }
            "√" -> applyFunction { sqrt(it) }
            else -> {
                currentInput += value
                display.text = currentInput
            }
        }
    }

    private fun calculateResult() {
        try {
            val result = eval(currentInput)
            display.text = result.toString()
            currentInput = result.toString()
        } catch (e: Exception) {
            display.text = "Error"
            currentInput = ""
        }
    }

    private fun applyFunction(func: (Double) -> Double) {
        try {
            val number = currentInput.toDouble()
            val result = func(number)
            display.text = result.toString()
            currentInput = result.toString()
        } catch (e: Exception) {
            display.text = "Error"
            currentInput = ""
        }
    }

    private fun eval(expression: String): Double {
        return object {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                var x: Double
                val startPos = pos
                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else {
                    while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                    x = expression.substring(startPos, pos).toDouble()
                }
                return x
            }
        }.parse()
    }
}