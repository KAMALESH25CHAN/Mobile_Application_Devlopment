package com.example.firstapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val main= findViewById<LinearLayout>(R.id.main)
        val textView = findViewById<TextView>(R.id.Text1)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        var fontsize: Float = 5f
        button2.setOnClickListener {
            textView.textSize=fontsize
            fontsize=fontsize+5
        }

        var col=0
        button1.setOnClickListener {
            when(col%4){
                0->textView.setTextColor(Color.RED)
                1->textView.setTextColor(Color.WHITE)
                2->textView.setTextColor(Color.BLACK)
                3->textView.setTextColor(Color.GREEN)
            }
            col++
        }
        var bgcol=0
        button3.setOnClickListener {
        when(bgcol%3){
            0->main.setBackgroundColor(Color.RED)
            1->main.setBackgroundColor(Color.WHITE)
            2->main.setBackgroundColor(Color.BLACK)
        }
            bgcol++
        }
    }
}
