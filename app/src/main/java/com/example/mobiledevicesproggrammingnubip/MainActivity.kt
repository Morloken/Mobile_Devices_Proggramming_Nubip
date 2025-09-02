package com.example.mobiledevicesproggrammingnubip
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val info = listOf(
                        "ПІБ" to "Ілларіонов Микита Іванович",
                        "Спеціальність" to "Інформаційні технології",
                        "Курс і група" to "3 курс, ІПЗ-23007Б",
                        "Ціль курсу" to "Навчитися створювати мобільні застосунки"
                    )
                    InfoScreen(info)
                }
            }
        }
    }
}
@Composable
fun InfoScreen(pairs: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Про мене",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        LazyColumn(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(pairs) { (label, value) ->
                InfoCard(label, value)
            }
        }
    }
}
@Composable
fun InfoCard(label: String, value: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.titleMedium, fontWeight =
                FontWeight.SemiBold)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top
            = 4.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMyInfoApp() {
    MaterialTheme {
        InfoScreen(
            listOf(
                "ПІБ" to "Тестовий Студент",
                "Спеціальність" to "ІПЗ",
                "Курс і група" to "3 курс, ІПЗ-23007Б",
                "Ціль" to "Зробити лабу"
            )
        )
    }
}