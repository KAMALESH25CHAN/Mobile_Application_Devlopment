package com.example.exp_05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exp_05.ui.theme.Exp05Theme

data class DailyExpense(val day: String, val amount: Float, val color: Color)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exp05Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FinancialDashboard(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FinancialDashboard(modifier: Modifier = Modifier) {
    var expenses by remember {
        mutableStateOf(
            listOf(
                DailyExpense("Mon", 130f, Color(0xFF81C784)),
                DailyExpense("Tue", 180f, Color(0xFFFFD54F)),
                DailyExpense("Wed", 90f, Color(0xFFE57373)),
                DailyExpense("Thu", 230f, Color(0xFF64B5F6)),
                DailyExpense("Fri", 300f, Color(0xFF4CAF50)),
                DailyExpense("Sat", 140f, Color(0xFFFFB74D)),
                DailyExpense("Sun", 210f, Color(0xFF9575CD))
            )
        )
    }

    var selectedDay by remember { mutableStateOf(expenses[0].day) }
    var amountText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val totalExpense = expenses.sumOf { it.amount.toDouble() }.toFloat()
    val highestSpending = expenses.maxByOrNull { it.amount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Financial Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Total Expense: $${String.format("%.2f", totalExpense)}", fontWeight = FontWeight.Bold)
                Text(text = "Highest Spending Day: ${highestSpending?.day} ($${highestSpending?.amount})")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedDay)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    expenses.forEach { expense ->
                        DropdownMenuItem(
                            text = { Text(expense.day) },
                            onClick = {
                                selectedDay = expense.day
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount ($)") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Button(
            onClick = {
                val newAmount = amountText.toFloatOrNull() ?: 0f
                expenses = expenses.map {
                    if (it.day == selectedDay) it.copy(amount = newAmount) else it
                }
                amountText = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
        ) {
            Text("Update All Charts")
        }

        ChartSection(title = "1. Bar Chart: Daily Comparison") {
            BarChart(data = expenses)
        }

        ChartSection(title = "2. Line Chart: Weekly Trend") {
            LineChart(data = expenses)
        }

        ChartSection(title = "3. Pie Chart: Spending Distribution") {
            PieChart(data = expenses)
        }
    }
}

@Composable
fun ChartSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun BarChart(data: List<DailyExpense>) {
    val maxAmount = (data.maxByOrNull { it.amount }?.amount ?: 0f).coerceAtLeast(1f)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val barWidth = width / (data.size * 2f)
        val space = barWidth

        // Draw Axes
        drawLine(Color.Gray, Offset(40.dp.toPx(), 0f), Offset(40.dp.toPx(), height - 30.dp.toPx()), strokeWidth = 2f)
        drawLine(Color.Gray, Offset(40.dp.toPx(), height - 30.dp.toPx()), Offset(width, height - 30.dp.toPx()), strokeWidth = 2f)

        data.forEachIndexed { index, expense ->
            val barHeight = (expense.amount / maxAmount) * (height - 60.dp.toPx())
            val x = 50.dp.toPx() + index * (barWidth + space)
            val y = height - 30.dp.toPx() - barHeight

            drawRect(
                color = expense.color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )

            // Labels and amounts can be drawn here using nativeCanvas or similar if needed
            // For simplicity, we just draw the bars.
        }
    }
}

@Composable
fun LineChart(data: List<DailyExpense>) {
    val maxAmount = (data.maxByOrNull { it.amount }?.amount ?: 0f).coerceAtLeast(1f)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val spacing = (width - 60.dp.toPx()) / (data.size - 1)

        val path = Path()
        val fillPath = Path()

        data.forEachIndexed { index, expense ->
            val x = 40.dp.toPx() + index * spacing
            val y = height - 30.dp.toPx() - (expense.amount / maxAmount) * (height - 60.dp.toPx())

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height - 30.dp.toPx())
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            if (index == data.size - 1) {
                fillPath.lineTo(x, height - 30.dp.toPx())
                fillPath.close()
            }
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF64B5F6).copy(alpha = 0.3f), Color.Transparent)
            )
        )

        drawPath(
            path = path,
            color = Color(0xFF1E88E5),
            style = Stroke(width = 4f)
        )

        data.forEachIndexed { index, expense ->
            val x = 40.dp.toPx() + index * spacing
            val y = height - 30.dp.toPx() - (expense.amount / maxAmount) * (height - 60.dp.toPx())
            drawCircle(Color(0xFF1E88E5), radius = 6f, center = Offset(x, y))
        }
    }
}

@Composable
fun PieChart(data: List<DailyExpense>) {
    val total = data.sumOf { it.amount.toDouble() }.toFloat()
    Canvas(modifier = Modifier.fillMaxSize()) {
        val chartSize = size.minDimension * 0.8f
        val topLeft = Offset((size.width - chartSize) / 2, (size.height - chartSize) / 2)
        var startAngle = 0f

        data.forEach { expense ->
            val sweepAngle = (expense.amount / total) * 360f
            drawArc(
                color = expense.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = topLeft,
                size = Size(chartSize, chartSize)
            )
            startAngle += sweepAngle
        }
        
        // Inner circle for donut effect if desired
        drawCircle(
            color = Color.White,
            radius = chartSize * 0.2f,
            center = center
        )
    }
}
