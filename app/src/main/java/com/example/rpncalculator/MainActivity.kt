package com.example.rpncalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Stack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RpnCalculatorApp()
        }
    }
}

class RpnCalculator {
    private val stack = Stack<Double>()

    fun push(value: Double) = stack.push(value)
    fun clear() = stack.clear()

    fun apply(op: String) {
        if (stack.size < 2) return
        val b = stack.pop()
        val a = stack.pop()
        stack.push(
            when (op) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> a
            }
        )
    }

    fun getStack(): List<Double> = stack.toList()
}

@Composable
fun RpnCalculatorApp() {
    val calc = remember { RpnCalculator() }
    var input by remember { mutableStateOf("") }
    var stack by remember { mutableStateOf(calc.getStack()) }

    fun refresh() { stack = calc.getStack() }

    MaterialTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Стек")

            Card {
                Column(Modifier.padding(8.dp)) {
                    if (stack.isEmpty()) Text("— пусто —")
                    else stack.reversed().forEach { Text(it.toString()) }
                }
            }

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Число") },
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    input.toDoubleOrNull()?.let {
                        calc.push(it)
                        input = ""
                        refresh()
                    }
                }) { Text("ENTER") }

                Button(
                    onClick = { calc.clear(); refresh() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("CLR") }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("+", "-", "*", "/").forEach {
                    Button(onClick = { calc.apply(it); refresh() }) {
                        Text(it)
                    }
                }
            }
        }
    }
}
