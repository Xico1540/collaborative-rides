package com.example.cmu_trabalho.ui.screens

import UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cmu_trabalho.R

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.database.FakeUserDao
import com.example.cmu_trabalho.database.UserDao
import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, userRepository: UserRepository, userViewModel: UserViewModel = viewModel()) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showErrorDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Boleias",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Colaborativas",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )
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
                        Text("e-mail", color = Color.Black)
                    }
                    innerTextField()
                }
            )
            BasicTextField(
                value = password.value,
                onValueChange = { password.value = it },
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
            Button(
                onClick = {
                    coroutineScope.launch {
                        val user = userRepository.getUserByEmail(email.value)
                        if (user != null && user.password == password.value) {
                            userViewModel.userId.value = user.userId
                            navController.navigate("home")
                        } else {
                            showErrorDialog.value = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Entrar", color = Color.White)
            }
            Text(
                text = "Registar",
                color = Color.Black,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                },
                textDecoration = TextDecoration.Underline
            )
        }

        if (showErrorDialog.value) {
            AlertDialog(
                onDismissRequest = { showErrorDialog.value = false },
                title = { Text("Erro") },
                text = { Text("Credenciais inválidas. Tente novamente.") },
                confirmButton = {
                    TextButton(
                        onClick = { showErrorDialog.value = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
@Composable
@Preview(showBackground = true)
fun LoginScreenPreview(){
    val userRepository = UserRepository(FakeUserDao())
    LoginScreen(navController = rememberNavController(),
        userRepository = userRepository)
}
