package com.example.cmu_trabalho.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cmu_trabalho.models.Car
import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.regex.Pattern
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cmu_trabalho.R
import com.example.cmu_trabalho.database.FakeUserDao
import com.example.cmu_trabalho.database.UserDao


@Composable
fun RegisterScreen(navController: NavController, userRepository: UserRepository) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // State for fields and errors
    val name = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val birthDate = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val pais = remember { mutableStateOf("") }
    val carMarca = remember { mutableStateOf("") }
    val carModelo = remember { mutableStateOf("") }
    val carLugares = remember { mutableStateOf(0f) }
    val carMatricula = remember { mutableStateOf("") }

    val isCarSectionExpanded = remember { mutableStateOf(false) }

    val errorMessages = remember { mutableStateOf(mapOf<String, String>()) }

    val showSuccessDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val userImageUri = remember { mutableStateOf<Uri?>(null) }
    val carImageUri = remember { mutableStateOf<Uri?>(null) }

    val pickUserImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            userImageUri.value = uri
        }
    val pickCarImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            carImageUri.value = uri
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Name Field
            BasicTextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                    if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "name"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (name.value.isEmpty()) {
                        Text("Nome", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["name"])

            Text("Foto do utilizador (opcional)", color = Color.Black)
            if (userImageUri.value != null) {
                Image(
                    painter = rememberAsyncImagePainter(userImageUri.value),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                )
            }
            Button(onClick = { pickUserImageLauncher.launch("image/*") }) {
                Text("Escolher Foto")
            }

            // Nationality Field
            BasicTextField(
                value = pais.value,
                onValueChange = {
                    pais.value = it
                    if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "pais"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (pais.value.isEmpty()) {
                        Text("País", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["pais"])


            // Email Field
            BasicTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "email"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (email.value.isEmpty()) {
                        Text("Email", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["email"])

            // Phone Field
            BasicTextField(
                value = phone.value,
                onValueChange = {
                    phone.value = it
                    if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "phone"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (phone.value.isEmpty()) {
                        Text("Número de Telefone", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["phone"])

            // Birth Date Field
            BasicTextField(
                value = birthDate.value,
                onValueChange = {
                    birthDate.value = it
                    if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "birthDate"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (birthDate.value.isEmpty()) {
                        Text("Data de Nascimento", color = Color.Black, modifier = Modifier
                            .clickable {
                                showDatePickerDialog(context, calendar) { day, month, year ->
                                    birthDate.value = "$day/$month/$year"
                                    errorMessages.value = errorMessages.value - "birthDate"
                                }
                            })
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["birthDate"])

            // Password Field
            BasicTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "password"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (password.value.isEmpty()) {
                        Text("Palavra-passe", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["password"])

            // Confirm Password Field
            BasicTextField(
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it
                    if (it.isNotEmpty()) errorMessages.value =
                        errorMessages.value - "confirmPassword"
                },
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (confirmPassword.value.isEmpty()) {
                        Text("Confirmar Palavra-passe", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            ErrorMessage(errorMessages.value["confirmPassword"])

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clickable {
                        isCarSectionExpanded.value = !isCarSectionExpanded.value
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Associar Carro", color = Color.Black)
                Icon(
                    imageVector = if (isCarSectionExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Car Section"
                )
            }

            // Car details section (expanded)
            if (isCarSectionExpanded.value) {
                Text("Foto do Carro (opcional)", color = Color.Black)
                if (carImageUri.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(carImageUri.value),
                        contentDescription = "Car Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    )
                }
                Button(onClick = { pickCarImageLauncher.launch("image/*") }) {
                    Text("Escolher Foto")
                }

                BasicTextField(
                    value = carMarca.value,
                    onValueChange = {
                        carMarca.value = it
                        if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "carMarca"
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (carMarca.value.isEmpty()) {
                            Text("Marca do Carro", color = Color.Black)
                        }
                        innerTextField()
                    }
                )
                ErrorMessage(errorMessages.value["carMarca"])

                BasicTextField(
                    value = carModelo.value,
                    onValueChange = {
                        carModelo.value = it
                        if (it.isNotEmpty()) errorMessages.value = errorMessages.value - "carModelo"
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (carModelo.value.isEmpty()) {
                            Text("Modelo do Carro", color = Color.Black)
                        }
                        innerTextField()
                    }
                )
                ErrorMessage(errorMessages.value["carModelo"])

                Text("Número de Lugares: ${carLugares.value.toInt()}", color = Color.Black)
                Slider(
                    value = carLugares.value,
                    onValueChange = {
                        carLugares.value = it
                        if (it > 0) errorMessages.value = errorMessages.value - "carLugares"
                    },
                    valueRange = 1f..10f,
                    steps = 9, // Passos entre 0 e 10 (sem incluir 10 como valor máximo)
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                ErrorMessage(errorMessages.value["carLugares"])

                BasicTextField(
                    value = carMatricula.value,
                    onValueChange = {
                        carMatricula.value = it
                        if (it.isNotEmpty()) errorMessages.value =
                            errorMessages.value - "carMatricula"
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (carMatricula.value.isEmpty()) {
                            Text("Matrícula do Carro", color = Color.Black)
                        }
                        innerTextField()
                    }
                )
                ErrorMessage(errorMessages.value["carMatricula"])
            }

            // Register Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        val existingUser = userRepository.getUserByEmail(email.value)
                        if (existingUser != null) {
                            showErrorDialog.value = true
                        } else {
                            val validationMessages = validateInput(
                                name.value,
                                address.value,
                                email.value,
                                phone.value,
                                birthDate.value,
                                password.value,
                                confirmPassword.value,
                                pais.value
                            )


                            val carValidationResult = validateCar(
                                carMarca.value,
                                carModelo.value,
                                carLugares.value.toInt(),
                                carMatricula.value,
                                isCarSectionExpanded.value
                            )

                            if (validationMessages.isEmpty() && carValidationResult == 0) {
                                val newUser = User(
                                    name = name.value,
                                    email = email.value,
                                    phone = phone.value,
                                    birthDate = birthDate.value,
                                    password = password.value,
                                    pais = pais.value,
                                    car = Car(
                                        Marca = carMarca.value,
                                        Matricula = carMatricula.value,
                                        Modelo = carModelo.value,
                                        lugares = carLugares.value.toInt(),
                                    )
                                )

                                userRepository.insert(newUser)
                                showSuccessDialog.value = true
                            } else if (validationMessages.isEmpty() && carValidationResult == 2) {
                                val newUser = User(
                                    name = name.value,
                                    email = email.value,
                                    phone = phone.value,
                                    birthDate = birthDate.value,
                                    password = password.value,
                                    pais = pais.value,
                                    car = null,
                                )

                                userRepository.insert(newUser)
                                Log.d("Register", "User without car saved successfully")
                                showSuccessDialog.value = true
                            } else {
                                Log.d("Register", "Validation failed, setting error messages")
                                errorMessages.value = validationMessages
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                Text("Registar", color = Color.White)
            }

        }



        if (showSuccessDialog.value) {
            SuccessDialog(
                onDismiss = {
                    showSuccessDialog.value = false
                    navController.navigate("login")
                }
            )
        }

        if (showErrorDialog.value) {
            ErrorDialog(
                onDismiss = { showErrorDialog.value = false },
                message = "O email fornecido já está registado."
            )
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Registo Bem-Sucedido", fontSize = 20.sp)
        },
        text = {
            Text("Seu registo foi realizado com sucesso.")
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Ir para o Login", color = Color.White)
            }
        }
    )
}

@Composable
fun ErrorDialog(onDismiss: () -> Unit, message: String) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Erro de Registo", fontSize = 20.sp) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("OK", color = Color.White)
            }
        }
    )
}


@Composable
fun ErrorMessage(errorMessage: String?) {
    if (!errorMessage.isNullOrEmpty()) {
        Text(
            text = errorMessage,
            color = Color(0xFFB00020),
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .fillMaxWidth(0.8f)
        )
    }
}

fun validateInput(
    name: String,
    address: String,
    email: String,
    phone: String,
    birthDate: String,
    password: String,
    confirmPassword: String,
    nacionalidade: String
): Map<String, String> {
    val errors = mutableMapOf<String, String>()

    if (name.isEmpty()) errors["name"] = "Nome é obrigatório"
    if (email.isEmpty()) errors["email"] = "Email é obrigatório"
    if (!isValidEmail(email)) errors["email"] = "Email inválido"
    if (phone.isEmpty()) errors["phone"] = "Número de Telefone é obrigatório"
    if (!isValidPhoneNumber(phone)) errors["phone"] = "Número de Telefone inválido"
    if (birthDate.isEmpty()) errors["birthDate"] = "Data de Nascimento é obrigatória"
    else if (!isAdult(birthDate)) errors["birthDate"] =
        "Deve ser maior de idade para se registar"
    if (password.isEmpty()) errors["password"] = "Senha é obrigatória"
    if (password.length < 6) errors["password"] = "A senha deve ter pelo menos 6 caracteres"
    if (password != confirmPassword) errors["confirmPassword"] = "As senhas não coincidem"
    if (nacionalidade.isEmpty()) errors["pais"] = "País é obrigatório"

    return errors
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
    return Pattern.matches(emailPattern, email)
}

fun isValidPhoneNumber(phone: String): Boolean {
    return phone.length == 9 && phone.startsWith("9") && phone.all { it.isDigit() }
}

@RequiresApi(Build.VERSION_CODES.N)
fun showDatePickerDialog(
    context: Context,
    calendar: Calendar,
    onDateSet: (Int, Int, Int) -> Unit
) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSet(dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

fun isAdult(birthDate: String): Boolean {
    val parts = birthDate.split("/").map { it.toInt() }
    val birthYear = parts[2]
    val birthMonth = parts[1]
    val birthDay = parts[0]

    val today = Calendar.getInstance()
    val age = today.get(Calendar.YEAR) - birthYear

    return if (age > 18) true
    else if (age == 18) {
        val currentMonth = today.get(Calendar.MONTH) + 1
        val currentDay = today.get(Calendar.DAY_OF_MONTH)
        birthMonth < currentMonth || (birthMonth == currentMonth && birthDay <= currentDay)
    } else false
}

fun validateCar(
    carMarca: String,
    carModelo: String,
    carLugares: Int,
    carMatricula: String,
    isCarSectionExpanded: Boolean
): Int {
    if (isCarSectionExpanded) {
        if (carMarca.isNotEmpty() && carModelo.isNotEmpty() && carLugares > 0 && carMatricula.isNotEmpty() && carMatricula.matches(
                "^[A-Z]{2}-\\d{2}-[A-Z]{2}$".toRegex()
            )
        ) {
            return 0;
        } else {
            return 1
        };
    } else {
        return 2
    };
}


@Preview(showBackground = true)
@Composable
fun PreviewSuccessDialog() {
    SuccessDialog(onDismiss = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    ErrorDialog(onDismiss = {}, message = "Erro de registo")
}


@Preview(showBackground = true)
@Composable
fun PreviewErrorMessage() {
    ErrorMessage("Este é um erro de exemplo")
}

@Preview(showBackground = true)
@Composable
fun PreviewEmailField() {
    val email = remember { mutableStateOf("") }
    BasicTextField(
        value = email.value,
        onValueChange = { email.value = it },
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(8.dp)),
        singleLine = true,
        decorationBox = { innerTextField ->
            if (email.value.isEmpty()) {
                Text("Email", color = Color.Black)
            }
            innerTextField()
        }
    )
    ErrorMessage("Email é obrigatório")
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController()
    val userRepository = UserRepository(FakeUserDao())// Supondo que você tenha um repositório de usuário simulado
    RegisterScreen(navController = navController, userRepository = userRepository)
}



