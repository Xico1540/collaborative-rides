package com.example.cmu_trabalho.ui.screens

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.getLocationDetailsFromCoordinates
import com.example.cmu_trabalho.models.Trip
import kotlinx.coroutines.launch

@Composable
fun EditTripScreen(
    navController: NavController
) {
    val trip = Trip(
        Origem = "Lisboa",
        Destino = "Porto",
        HoraPartida = "08:00",
        HoraChegada = "10:30",
        Regras = "Proibido fumar",
        lugaresDisponiveis = 10
    )
    val context = LocalContext.current

    // Campos de entrada com valores da viagem a ser editada
    var origem by remember { mutableStateOf(trip.Origem) }
    var destino by remember { mutableStateOf(trip.Destino) }
    var partida by remember { mutableStateOf(trip.HoraPartida) }
    var chegada by remember { mutableStateOf(trip.HoraChegada) }
    var preco by remember { mutableStateOf(trip.lugaresDisponiveis.toString()) }
    var regras by remember { mutableStateOf(trip.Regras) }

    var origemError by remember { mutableStateOf<String?>(null) }
    var destinoError by remember { mutableStateOf<String?>(null) }
    var partidaError by remember { mutableStateOf<String?>(null) }
    var chegadaError by remember { mutableStateOf<String?>(null) }
    var precoError by remember { mutableStateOf<String?>(null) }
    var origemLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var destinoLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Pair<Double, Double>>("origemLatLng")
            ?.observeForever { latLng ->
                origemLatLng = latLng
            }

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Pair<Double, Double>>("destinoLatLng")
            ?.observeForever { latLng ->
                destinoLatLng = latLng
            }
    }

    LaunchedEffect(origemLatLng) {
        origemLatLng?.let { latLng ->
            val locationDetails = getLocationDetailsFromCoordinates(latLng.first, latLng.second, context)
            origem = locationDetails ?: "Desconhecido"
        }
    }

    LaunchedEffect(destinoLatLng) {
        destinoLatLng?.let { latLng ->
            val locationDetails = getLocationDetailsFromCoordinates(latLng.first, latLng.second, context)
            destino = locationDetails ?: "Desconhecido"
        }
    }

    // Função para abrir o TimePicker
    fun showTimePicker(isPartida: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                if (isPartida) {
                    partida = formattedTime
                    partidaError = null
                } else {
                    chegada = formattedTime
                    chegadaError = null
                }
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    fun openMapSelection(isPartida: Boolean) {
        navController.navigate("map_selection/${isPartida}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Editar Viagem",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            item {
                OutlinedTextField(
                    value = origem,
                    onValueChange = { origem = it },
                    label = { Text("Origem") },
                    isError = origemError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (origemError != null) {
                    Text(origemError!!, color = Color.Red, fontSize = 12.sp)
                }
            }

            item {
                OutlinedButton(
                    onClick = { openMapSelection(isPartida = true) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Selecionar Origem: ${origemLatLng?.let { "Selecionado!" } ?: "Escolha no mapa"}")
                }
            }


            item {
                OutlinedTextField(
                    value = destino,
                    onValueChange = { destino = it },
                    label = { Text("Destino") },
                    isError = destinoError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (destinoError != null) {
                    Text(destinoError!!, color = Color.Red, fontSize = 12.sp)
                }
            }


            item {
                OutlinedButton(
                    onClick = { openMapSelection(isPartida = false) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Selecionar Destino: ${destinoLatLng?.let { "Selecionado!" } ?: "Escolha no mapa"}")
                }
            }

            item {
                OutlinedButton(
                    onClick = { showTimePicker(isPartida = true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Horário de Partida: ${partida.ifEmpty { "Selecione" }}")
                }
                if (partidaError != null) {
                    Text(partidaError!!, color = Color.Red, fontSize = 12.sp)
                }
            }

            item {
                OutlinedButton(
                    onClick = { showTimePicker(isPartida = false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Horário de Chegada: ${chegada.ifEmpty { "Selecione" }}")
                }
                if (chegadaError != null) {
                    Text(chegadaError!!, color = Color.Red, fontSize = 12.sp)
                }
            }

            item {
                OutlinedTextField(
                    value = preco,
                    onValueChange = {
                        preco = it
                        precoError = null
                    },
                    label = { Text("Preço por Lugar (€)") },
                    isError = precoError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (precoError != null) {
                    Text(precoError!!, color = Color.Red, fontSize = 12.sp)
                }
            }

            item {
                OutlinedTextField(
                    value = regras,
                    onValueChange = { regras = it },
                    label = { Text("Regras (Ex: Proibido Fumar)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                val coroutineScope = rememberCoroutineScope()

                Button(
                    onClick = {
                        // Resetar erros antes da validação
                        origemError = if (origem.isEmpty()) "Campo obrigatório" else null
                        destinoError = if (destino.isEmpty()) "Campo obrigatório" else null
                        partidaError = if (partida.isEmpty()) "Campo obrigatório" else null
                        chegadaError = if (chegada.isEmpty()) "Campo obrigatório" else null
                        precoError = if (preco.isEmpty() || preco.toDoubleOrNull() == null || preco.toDouble() <= 0)
                            "Preço inválido" else null

                        if (origemError == null && destinoError == null && partidaError == null &&
                            chegadaError == null && precoError == null) {

                            // Simula a edição da viagem (sem persistir)
                            val updatedTrip = trip.copy(
                                Origem = origem,
                                Destino = destino,
                                HoraPartida = partida,
                                HoraChegada = chegada,
                                Regras = regras,
                                lugaresDisponiveis = preco.toInt()
                            )

                            coroutineScope.launch {
                                Toast.makeText(context, "Viagem editada com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Por favor, corrija os erros nos campos.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Salvar Alterações",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditScreenPreview(){
    EditTripScreen(navController = rememberNavController())
}