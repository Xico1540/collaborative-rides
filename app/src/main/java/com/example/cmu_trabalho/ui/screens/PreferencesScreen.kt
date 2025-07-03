package com.example.cmu_trabalho.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun PreferencesScreen(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Botão para alternar entre Dark Mode e Light Mode
        Text(
            text = "Dark Mode",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Switch(
            checked = currentTheme,
            onCheckedChange = { newTheme ->
                onThemeChange(newTheme)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Opções de idioma
        Text(
            text = "Idioma",
            style = MaterialTheme.typography.bodyLarge,  // Usando bodyLarge
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    onLanguageChange("en")  // Muda para inglês
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("English")
            }
            Button(
                onClick = {
                    onLanguageChange("pt")  // Muda para português
                }
            ) {
                Text("Português")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreferencesScreen() {
    var isDarkMode by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("en") }

    PreferencesScreen(
        currentTheme = isDarkMode,
        onThemeChange = { isDarkMode = it },
        onLanguageChange = { language = it }
    )
}