package com.example.cmu_trabalho.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class Trip(
    val id: Int,
    val origin: String,
    val destination: String,
    val date: String,
    val price: Double,
    val driverName: String
)

@Composable
fun TripsScreen(navController: NavController) {
    // Exemplo de lista de viagens
    val trips = listOf(
        Trip(1, "Porto", "Lisboa", "10-11-2024", 15.0, "João Silva"),
        Trip(2, "Braga", "Porto", "11-11-2024", 7.5, "Ana Costa"),
        Trip(3, "Lisboa", "Faro", "12-11-2024", 20.0, "Carlos Pinto"),
        Trip(4, "Lisboa", "Faro", "12-11-2024", 20.0, "Carlos Pinto"),
        Trip(5, "Lisboa", "Faro", "12-11-2024", 20.0, "Carlos Pinto")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Procurar Viagens",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TripFilters()

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de viagens
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trips.size) { index ->
                TripItem(trip = trips[index], navController)
            }
        }
    }
}

@Composable
fun TripFilters() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BasicTextField(
            value = "Porto",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                        .padding(8.dp)
                ) {
                    innerTextField()
                }
            }
        )
        BasicTextField(
            value = "Lisboa",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                        .padding(8.dp)
                ) {
                    innerTextField()
                }
            }
        )
        Button(onClick = { /* Adicionar lógica de filtro aqui */ }) {
            Text("Filtrar")
        }
    }
}


@Composable
fun TripItem(trip: Trip, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Origem: ${trip.origin}", style = MaterialTheme.typography.bodyMedium)
            Text("Destino: ${trip.destination}", style = MaterialTheme.typography.bodyMedium)
            Text("Data: ${trip.date}", style = MaterialTheme.typography.bodyMedium)
            Text("Preço: ${trip.price} €", style = MaterialTheme.typography.bodyMedium)
            Text("Condutor: ${trip.driverName}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.navigate("details_trip") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver Detalhes")
                }

                Button(
                    onClick = { /* Ação para solicitar boleia */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Solicitar boleia")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    TripsScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun TripFiltersPreview() {
    TripFilters()
}

@Preview(showBackground = true)
@Composable
fun TripItemPreview() {
    val trip = Trip(1, "Porto", "Lisboa", "10-11-2024", 15.0, "João Silva")
    TripItem(trip = trip, navController = rememberNavController())
}







