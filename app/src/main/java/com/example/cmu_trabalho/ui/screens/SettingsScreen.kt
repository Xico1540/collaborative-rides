package com.example.cmu_trabalho.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.models.Car

@Composable
fun SettingsScreen(navController: NavController, user: User) {
    // Estados para informações do usuário
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var pais by remember { mutableStateOf(user.pais) }
    var birthDate by remember { mutableStateOf(user.birthDate) }

    // Estados para informações do carro
    var carBrand by remember { mutableStateOf(user.car?.Marca ?: "") }
    var carModel by remember { mutableStateOf(user.car?.Modelo ?: "") }
    var carSeats by remember { mutableStateOf(user.car?.lugares?.toString() ?: "") }
    var carPlate by remember { mutableStateOf(user.car?.Matricula ?: "") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos de informações do usuário
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = pais,
            onValueChange = { pais = it },
            label = { Text("País") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Data de Nascimento") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de informações do carro (se o carro existir)
        Text(
            text = "Informações do Carro",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = carBrand,
            onValueChange = { carBrand = it },
            label = { Text("Marca") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = carModel,
            onValueChange = { carModel = it },
            label = { Text("Modelo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = carSeats,
            onValueChange = { carSeats = it },
            label = { Text("Lugares") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = carPlate,
            onValueChange = { carPlate = it },
            label = { Text("Matrícula") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botões de salvar e logout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    // Atualize o objeto do usuário com os novos valores e salve
                    val updatedUser = user.copy(
                        name = name,
                        email = email,
                        phone = phone,
                        pais = pais,
                        birthDate = birthDate,
                        car = Car(
                            Marca = carBrand,
                            Modelo = carModel,
                            lugares = carSeats.toIntOrNull() ?: 0,
                            Matricula = carPlate
                        )
                    )
                    // Lógica para salvar o usuário atualizado
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Salvar")
            }

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController(), user = User(
        userId = 1L,
        name = "Bruno Pontvianne",
        email = "bruno.pontvianne2004@gmail.com",
        phone = "912345678",
        password = "password123",
        pais = "Portugal",
        birthDate = "2004-01-01",
        car = Car(
            Marca = "Toyota",
            Modelo = "Corolla",
            lugares = 5,
            Matricula = "12-AB-34"
        )
    )
    )
}

