package com.example.bmi_calculator

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
        val weight = findViewById<EditText>(R.id.w)
        val height = findViewById<EditText>(R.id.h)
        val button = findViewById<Button>(R.id.b)
        val result = findViewById<TextView>(R.id.a)
        button.setOnClickListener {
            if(weight.text.toString().isNotEmpty() && height.text.toString().isNotEmpty()){
                val h = height.text.toString().toFloat()/100
                val w = weight.text.toString().toFloat()
                val bmi = w / (h*h)
                if (w >0 && h>0){
                    result.text = "Your BMI is %.2f".format(bmi)
                }
            }
            else{
                result.text ="Enter the Proper Values"
            }
        }
    }
}