package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.bai4.R

class MainActivity : AppCompatActivity() {

    private lateinit var amountSource: EditText
    private lateinit var amountTarget: EditText
    private lateinit var currencySource: Spinner
    private lateinit var currencyTarget: Spinner
    private var isSourceActive = true // Xác định EditText nào đang là nguồn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các view
        amountSource = findViewById(R.id.amountSource)
        amountTarget = findViewById(R.id.amountTarget)
        currencySource = findViewById(R.id.currencySource)
        currencyTarget = findViewById(R.id.currencyTarget)

        // Tạo danh sách các loại tiền và gán vào Spinner
        val currencies = arrayOf("USD", "EUR", "VND")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        currencySource.adapter = adapter
        currencyTarget.adapter = adapter

        // Lắng nghe thay đổi trên EditText và Spinner
        amountSource.addTextChangedListener(createTextWatcher())
        amountTarget.addTextChangedListener(createTextWatcher())
        currencySource.onItemSelectedListener = createSpinnerListener()
        currencyTarget.onItemSelectedListener = createSpinnerListener()

        // Lắng nghe sự thay đổi focus trên EditText
        amountSource.setOnFocusChangeListener { _, hasFocus -> isSourceActive = hasFocus }
        amountTarget.setOnFocusChangeListener { _, hasFocus -> isSourceActive = !hasFocus }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun createSpinnerListener() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            convertCurrency()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private fun convertCurrency() {
        // Lấy tỷ giá chuyển đổi từ đồng tiền nguồn sang đồng tiền đích
        val rate = getConversionRate(currencySource.selectedItem.toString(), currencyTarget.selectedItem.toString())

        if (isSourceActive) {
            // Nếu người dùng nhập ở `amountSource`, chuyển đổi số tiền từ `source` sang `target`
            val sourceAmount = amountSource.text.toString().toDoubleOrNull() ?: 0.0
            amountTarget.setText((sourceAmount * rate).toString())
        } else {
            // Nếu người dùng nhập ở `amountTarget`, chuyển đổi ngược lại từ `target` sang `source`
            val targetAmount = amountTarget.text.toString().toDoubleOrNull() ?: 0.0
            amountSource.setText((targetAmount / rate).toString())
        }
    }

    private fun getConversionRate(source: String, target: String): Double {
        // Giả định lấy tỷ giá từ dữ liệu mẫu
        return when (source to target) {
            "USD" to "EUR" -> 0.85
            "EUR" to "USD" -> 1.17
            "USD" to "VND" -> 24000.0
            "VND" to "USD" -> 1 / 24000.0
            "EUR" to "VND" -> 28000.0
            "VND" to "EUR" -> 1 / 28000.0
            else -> 1.0 // Mặc định nếu không tìm thấy tỷ giá
        }
    }
}
