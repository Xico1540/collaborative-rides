package com.example.cmu_trabalho.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.models.Trip
import com.example.cmu_trabalho.models.UserTrip
import com.example.cmu_trabalho.models.enums.Role
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



@Composable
fun MyTripsScreen(navController: NavHostController) {
    val currentDate = LocalDateTime.now()

    val allTrips = listOf(
        Trip(tripId = 1, Origem = "Porto", Destino = "Lisboa", HoraPartida = "2024-11-20 08:00", HoraChegada = "2024-11-20 12:00", Regras = "", lugaresDisponiveis = 4),
        Trip(tripId = 2, Origem = "Lisboa", Destino = "Porto", HoraPartida = "2024-11-25 14:00", HoraChegada = "2024-11-25 18:00", Regras = "", lugaresDisponiveis = 2),
        Trip(tripId = 3, Origem = "Porto", Destino = "Braga", HoraPartida = "2024-11-10 10:00", HoraChegada = "2024-11-10 11:00", Regras = "", lugaresDisponiveis = 0),
        Trip(tripId = 4, Origem = "Braga", Destino = "Porto", HoraPartida = "2024-11-12 18:00", HoraChegada = "2024-11-12 19:00", Regras = "", lugaresDisponiveis = 0),
        Trip(tripId = 5, Origem = "Lisboa", Destino = "Aveiro", HoraPartida = "2024-12-05 07:00", HoraChegada = "2024-12-05 09:30", Regras = "", lugaresDisponiveis = 3),
        Trip(tripId = 6, Origem = "Braga", Destino = "Coimbra", HoraPartida = "2024-12-10 10:30", HoraChegada = "2024-12-10 12:00", Regras = "", lugaresDisponiveis = 0),
        Trip(tripId = 7, Origem = "Coimbra", Destino = "Porto", HoraPartida = "2024-11-30 16:00", HoraChegada = "2024-11-30 18:00", Regras = "", lugaresDisponiveis = 5),
        Trip(tripId = 8, Origem = "Funchal", Destino = "Lisboa", HoraPartida = "2024-12-20 18:00", HoraChegada = "2024-12-20 22:00", Regras = "", lugaresDisponiveis = 2),
        Trip(tripId = 9, Origem = "Lisboa", Destino = "Porto", HoraPartida = "2024-11-29 14:00", HoraChegada = "2024-11-29 18:00", Regras = "", lugaresDisponiveis = 1),
        Trip(tripId = 10, Origem = "Porto", Destino = "Viseu", HoraPartida = "2024-11-15 08:00", HoraChegada = "2024-11-15 10:00", Regras = "", lugaresDisponiveis = 2)
    )

    val userTrips = listOf(
        UserTrip(userId = 1, tripId = 1, role = Role.CONDUTOR),
        UserTrip(userId = 1, tripId = 2, role = Role.PASSAGEIRO),
        UserTrip(userId = 1, tripId = 3, role = Role.PASSAGEIRO),
        UserTrip(userId = 1, tripId = 4, role = Role.CONDUTOR),
        UserTrip(userId = 1, tripId = 5, role = Role.CONDUTOR),
        UserTrip(userId = 1, tripId = 6, role = Role.PASSAGEIRO),
        UserTrip(userId = 1, tripId = 7, role = Role.CONDUTOR),
        UserTrip(userId = 1, tripId = 8, role = Role.PASSAGEIRO),
        UserTrip(userId = 1, tripId = 9, role = Role.CONDUTOR),
        UserTrip(userId = 1, tripId = 10, role = Role.PASSAGEIRO)
    )

    val futureTrips = allTrips.filter { it.HoraPartida > currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) }
    val pastTrips = allTrips.filter { it.HoraPartida <= currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) }

    val futureUserTrips = userTrips.filter { userTrip -> futureTrips.any { it.tripId == userTrip.tripId } }
    val pastUserTrips = userTrips.filter { userTrip -> pastTrips.any { it.tripId == userTrip.tripId } }

    var selectedRole by remember { mutableStateOf(Role.PASSAGEIRO) }
    var selectedTripTime by remember { mutableStateOf("aRealizar") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Seletor de tipos de viagem com Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(
                selected = selectedRole == Role.CONDUTOR,
                onClick = { selectedRole = Role.CONDUTOR },
                label = { Text("Como Condutor") },
                modifier = Modifier.padding(4.dp)
            )
            Chip(
                selected = selectedRole == Role.PASSAGEIRO,
                onClick = { selectedRole = Role.PASSAGEIRO },
                label = { Text("Como Passageiro") },
                modifier = Modifier.padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seletor de viagens a realizar ou realizadas
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(
                selected = selectedTripTime == "aRealizar",
                onClick = { selectedTripTime = "aRealizar" },
                label = { Text("Viagens a Realizar") },
                modifier = Modifier.padding(4.dp)
            )
            Chip(
                selected = selectedTripTime == "realizadas",
                onClick = { selectedTripTime = "realizadas" },
                label = { Text("Viagens Realizadas") },
                modifier = Modifier.padding(4.dp)
            )
        }

// Renderizando as viagens com base na seleção
        if (selectedTripTime == "aRealizar") {
            RenderTrips(
                title = "Viagens a Realizar",
                trips = if (selectedRole == Role.CONDUTOR) futureUserTrips.filter { it.role == Role.CONDUTOR }
                else futureUserTrips.filter { it.role == Role.PASSAGEIRO },
                allTrips = allTrips,
                navController = navController,
                selectedRole = selectedRole,
                selectedTripTime = selectedTripTime
            )
        } else {
            RenderTrips(
                title = "Viagens Realizadas",
                trips = if (selectedRole == Role.CONDUTOR) pastUserTrips.filter { it.role == Role.CONDUTOR }
                else pastUserTrips.filter { it.role == Role.PASSAGEIRO },
                allTrips = allTrips,
                navController = navController,
                selectedRole = selectedRole,
                selectedTripTime = selectedTripTime
            )
        }
    }
}

@Composable
fun Chip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            label()
        }
    }
}

@Composable
fun RenderTrips(
    title: String,
    trips: List<UserTrip>,
    allTrips: List<Trip>,
    navController: NavHostController,
    selectedRole: Role,
    selectedTripTime: String
) {
    Text(text = "$title (${trips.size})", style = MaterialTheme.typography.bodyMedium)
    LazyColumn() {
        items(trips) { userTrip ->
            val trip = allTrips.find { it.tripId == userTrip.tripId }
            trip?.let {
                TripItem(navController = navController,
                    trip = it,
                    onClick = {
                        when {
                            // Condutor e viagem a realizar
                            selectedRole == Role.CONDUTOR && selectedTripTime == "aRealizar" -> {
                                navController.navigate("edit_trip")
                            }
                            // Condutor e viagem realizada
                            selectedRole == Role.CONDUTOR && selectedTripTime == "realizadas" -> {
                                navController.navigate("view_reviews")
                            }
                            // Passageiro e viagem a realizar
                            selectedRole == Role.PASSAGEIRO && selectedTripTime == "aRealizar" -> {
                                navController.navigate("cancel_trip")
                            }
                            // Passageiro e viagem realizada
                            selectedRole == Role.PASSAGEIRO && selectedTripTime == "realizadas" -> {
                                navController.navigate("rate_trip")
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun TripItem(trip: Trip, navController: NavController, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Origem: ${trip.Origem}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Destino: ${trip.Destino}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Hora de Partida: ${trip.HoraPartida}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Hora de Chegada: ${trip.HoraChegada}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Lugares Disponíveis: ${trip.lugaresDisponiveis}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                navController.navigate("details_trip")
            }) {
                Text("Ver Detalhes")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTripItem(){
    TripItem(
        Trip(tripId = 1, Origem = "Porto", Destino = "Lisboa", HoraPartida = "2024-11-20 08:00", HoraChegada = "2024-11-20 12:00", Regras = "", lugaresDisponiveis = 4),
        navController = rememberNavController(),
        onClick = {})
      }

@Preview(showBackground = true)
@Composable
fun MyTripScreenPreview(){
   MyTripsScreen(navController = rememberNavController())
}


@Preview
@Composable
fun RenderTripsPreview(){
    val currentDate = LocalDateTime.now()
    val allTrips = listOf(
        Trip(tripId = 1, Origem = "Porto", Destino = "Lisboa", HoraPartida = "2024-11-20 08:00", HoraChegada = "2024-11-20 12:00", Regras = "", lugaresDisponiveis = 4),
        Trip(tripId = 2, Origem = "Lisboa", Destino = "Porto", HoraPartida = "2024-11-25 14:00", HoraChegada = "2024-11-25 18:00", Regras = "", lugaresDisponiveis = 2),
        Trip(tripId = 3, Origem = "Porto", Destino = "Braga", HoraPartida = "2024-11-10 10:00", HoraChegada = "2024-11-10 11:00", Regras = "", lugaresDisponiveis = 0)
    )
    val userTrips = listOf(
        UserTrip(userId = 1, tripId = 1, role = Role.CONDUTOR),
        UserTrip(userId = 1, tripId = 2, role = Role.PASSAGEIRO),
        UserTrip(userId = 1, tripId = 3, role = Role.PASSAGEIRO)
    )

    val futureTrips = allTrips.filter { it.HoraPartida > currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) }

    val futureUserTrips = userTrips.filter { userTrip -> futureTrips.any { it.tripId == userTrip.tripId } }

    RenderTrips(title = "Viagens a Realizar",
        trips = futureUserTrips,
        allTrips = allTrips,
        navController = rememberNavController(),
        selectedRole = Role.CONDUTOR,
        selectedTripTime = "realizadas")
}

@Preview(showBackground = true)
@Composable
fun ChipPreview(){
    var selectedRole = Role.CONDUTOR
    Chip( selected = selectedRole == Role.CONDUTOR,
        onClick = { selectedRole = Role.CONDUTOR },
        label = { Text("Como Condutor") },
        modifier = Modifier.padding(4.dp))
}

@Preview(showBackground = true)
@Composable
fun ChipPreview2(){
    var selectedRole = Role.PASSAGEIRO
    Chip( selected = selectedRole == Role.CONDUTOR,
        onClick = { selectedRole = Role.CONDUTOR },
        label = { Text("Como Condutor") },
        modifier = Modifier.padding(4.dp))
}