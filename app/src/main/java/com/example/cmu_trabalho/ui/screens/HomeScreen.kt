package com.example.cmu_trabalho.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.R

@Composable
fun HomeScreen(navController: NavController, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    // Aplica o tema dinâmico baseado no isDarkTheme
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background), // Usa a cor de fundo do tema
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Bem-vindo ao Boleias Colaborativas!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground // Usa a cor de texto do tema
                )
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Text(
                    text = "Esta aplicação permite que você compartilhe boleias com outros colaboradores de forma fácil e segura." +
                            " Junte-se a nós e comece a economizar tempo e dinheiro enquanto ajuda o meio ambiente!",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground // Usa a cor de texto do tema
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botão para Navegar para a Tela de Viagens
                Button(
                    onClick = { navController.navigate("trips") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Ver Viagens disponíveis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary // Cor do texto do botão
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Botão para navegar para a tela de criação de viagens
                Button(
                    onClick = { navController.navigate("create_trip") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Criar Nova Viagem",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary // Cor do texto do botão
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController(),
        false,
        onThemeChange = {})
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview_DarkMode(){
    HomeScreen(navController = rememberNavController(),
        true,
        onThemeChange = {})
}