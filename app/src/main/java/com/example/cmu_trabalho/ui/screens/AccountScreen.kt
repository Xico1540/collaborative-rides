package pt.ipp.estg.androidintentscompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.R
import com.example.cmu_trabalho.database.FakeUserDao
import com.example.cmu_trabalho.models.Car
import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.repository.UserRepository
import kotlinx.coroutines.runBlocking

@Composable
fun AccountScreen(
    navController: NavController,
    userRepository: UserRepository,
    userId: Long
) {
    val userState = remember { mutableStateOf<User?>(null) }

    LaunchedEffect(userId) {
        userState.value = userRepository.getUserById(userId)
    }

    userState.value?.let { user ->
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Header com imagem de fundo e botão de configurações
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.estg),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                                )
                            )
                    )
                    FloatingActionButton(
                        onClick = { navController.navigate("settings") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-16).dp, y = 16.dp)
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }

                // Corpo do perfil
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    // Nome e rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Row {
                            repeat(4) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Detalhes do perfil
                    AccountDetailRow("Email", user.email)
                    AccountDetailRow("Phone", user.phone)
                    AccountDetailRow("País", user.pais)
                    AccountDetailRow("Data de nascimento", user.birthDate)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Informações do carro (se disponível)
                    user.car?.let { car ->
                        Text(
                            text = "Informações do Carro:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AccountDetailRow("Marca", car.Marca)
                        AccountDetailRow("Modelo", car.Modelo)
                        AccountDetailRow("Lugares", car.lugares.toString())
                        AccountDetailRow("Matrícula", car.Matricula)
                    }
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun AccountDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccountDetailRowPreview() {
    AccountDetailRow("Email", "migueltav5@gmail.com")
}

@Preview(showBackground = true)
@Composable
fun AccountDetailRowPreview2() {
    AccountDetailRow("Phone", "999999999")
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    val fakeUser = User(
        userId = 1,
        name = "Miguel",
        email = "migueltav5@gmail.com",
        phone = "999999999",
        password = "111111",
        pais = "Portugal",
        birthDate = "01/01/1990",
        car = Car(
            Marca = "Toyota",
            Modelo = "Corolla",
            lugares = 5,
            Matricula = "AA-00-00"
        )
    )

    val userRepository = UserRepository(FakeUserDao())
    runBlocking {
        userRepository.insert(fakeUser) // Insere o usuário na fake base de dados
    }

    AccountScreen(
        navController = rememberNavController(),
        userRepository = userRepository,
        userId = fakeUser.userId
    )
}


