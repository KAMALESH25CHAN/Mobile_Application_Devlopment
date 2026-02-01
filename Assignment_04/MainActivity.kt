package com.example.tempertaure_converter

import android.os.Bundle
import android.widget.*;
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val input = findViewById<EditText>(R.id.i)
        val button = findViewById<Button>(R.id.b)
        val result  = findViewById<TextView>(R.id.result)
        val rg= findViewById<RadioGroup>(R.id.g)

        button.setOnClickListener {
            val check = input.text.toString()
            val id = rg.checkedRadioButtonId
           if (check.isNotEmpty() && id !=-1) {
               val temp = check.toFloat()
               val r = when (id) {
                   R.id.c -> (temp * 9 / 5) + 32
                   R.id.f -> (temp - 32) * 5 / 9
                   else -> 0f
               }
               result.text = "Your Output is %.2f".format(r)
           } else{
               result.text = "Enter the Temperature \n Select the Type !"
           }


    }
}