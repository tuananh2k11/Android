package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var resultTextView: TextView
    private var currentNumber: String = ""
    private var operation: String = ""
    private var result: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)

        val buttonIds = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide,
            R.id.buttonEquals, R.id.buttonClear, R.id.buttonClearEntry, R.id.buttonBackspace
        )

        buttonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        when (buttonText) {
            "=" -> calculateResult()
            "C" -> clearAll()
            "CE" -> clearEntry()
            "BS" -> backspace()
            "+", "-", "x", "/" -> setOperation(buttonText)
            else -> addDigit(buttonText)
        }

        updateDisplay()
    }

    private fun addDigit(digit: String) {
        currentNumber += digit
    }

    private fun setOperation(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (result != 0) {
                calculateResult()
            } else {
                result = currentNumber.toInt()
            }
            currentNumber = ""
        }
        operation = op
    }

    private fun calculateResult() {
        if (currentNumber.isNotEmpty() && operation.isNotEmpty()) {
            val secondNumber = currentNumber.toInt()
            result = when (operation) {
                "+" -> result + secondNumber
                "-" -> result - secondNumber
                "x" -> result * secondNumber
                "/" -> if (secondNumber != 0) result / secondNumber else 0
                else -> result
            }
            currentNumber = result.toString()
            operation = ""
        }
    }

    private fun clearAll() {
        currentNumber = ""
        operation = ""
        result = 0
    }

    private fun clearEntry() {
        currentNumber = ""
    }

    private fun backspace() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
        }
    }

    private fun updateDisplay() {
        resultTextView.text = when {
            currentNumber.isNotEmpty() -> currentNumber
            result != 0 -> result.toString()
            else -> "0"
        }
    }
}
