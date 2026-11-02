package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalc.ui.theme.TipCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcTheme {
                TipcalcScreen()
            }
        }
    }
}

@Composable
fun TipcalcScreen(){
    var orderAmount by remember { mutableStateOf("") }
    var  dishesCount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(10f) }

    val discountOptions = listOf(3,5,7,10)

    val dishCount = dishesCount.toIntOrNull() ?: 0
    val selectedDiscount = when {
        dishCount in 1..2 -> 3
        dishCount in 3..5 -> 5
        dishCount in 6..10 -> 7
        dishCount > 10 -> 10
        else -> 10
    }

    val amount = orderAmount
        .replace(',', '.')
        .toDoubleOrNull() ?: 0.0
    // Расчёт скидки
    val discountAmount = amount * selectedDiscount / 100
    // Сумма заказа с учётом скидки
    val discountedAmount = amount - discountAmount
    // Расчёт чаевых
    val tipAmount = discountedAmount * tipPercent / 100
    // Итоговая сумма
    val totalAmount = discountedAmount + tipAmount

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Калькулятор чаевых",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Column {
                Text(
                    text = "Сумма заказа",
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = orderAmount,
                    onValueChange = { orderAmount = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Введите сумму заказа") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )
            }

            Column {
                Text(
                    text = "Количество блюд",
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dishesCount,
                    onValueChange = { dishesCount = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Введите количество блюд") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }

            Column{
                Text(
                    text = "Чаевые: ${tipPercent.toInt()}% ",
                    fontWeight = FontWeight.Medium
                )
                Slider(
                    value = tipPercent,
                    onValueChange = { tipPercent = it },
                    valueRange = 0f..25f,
                    steps = 24
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0%")
                    Text("25%")
                }
            }

            Column {
                Text(
                    text = "Скидка:",
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    discountOptions.forEach { discount ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RadioButton(
                                selected = discount == selectedDiscount,
                                onClick = null
                            )

                            Text(
                                text = "$discount%",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            // Результаты расчёта
            if (amount > 0) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Скидка: %.2f ₽".format(discountAmount)
                    )
                    Text(
                        text = "Сумма со скидкой: %.2f ₽".format(discountedAmount)
                    )
                    Text(
                        text = "Чаевые: %.2f ₽".format(tipAmount)
                    )
                    Text(
                        text = "Итого к оплате: %.2f ₽".format(totalAmount),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalcTheme {
        TipcalcScreen()
    }
}