package com.example.cmu_trabalho.ui.screens

import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.util.Base64
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.database.FakeTripDao
import com.example.cmu_trabalho.database.FakeUserDao
import com.example.cmu_trabalho.getLocationDetailsFromCoordinates
import com.example.cmu_trabalho.models.Trip
import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.models.UserTrip
import com.example.cmu_trabalho.models.enums.Role
import com.example.cmu_trabalho.repository.TripRepository
import com.example.cmu_trabalho.repository.UserRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun CreateTripScreen(navController: NavController, tripRepository: TripRepository, userRepository: UserRepository, userId: Long) {
    val context = LocalContext.current

    var userDetails by remember { mutableStateOf<User?>(null) }

    // Buscar detalhes do usuário ao inicializar
    LaunchedEffect(userId) {
        userDetails = userRepository.getUserById(userId)
    }

    // Campos de entrada
    var origem by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var partida by remember { mutableStateOf("") }
    var chegada by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var regras by remember { mutableStateOf("") }
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



    var qrCodeBitmap: Bitmap? by remember { mutableStateOf(null) }
    var showPopup by remember { mutableStateOf(false) }

    var origemError by remember { mutableStateOf<String?>(null) }
    var destinoError by remember { mutableStateOf<String?>(null) }
    var partidaError by remember { mutableStateOf<String?>(null) }
    var chegadaError by remember { mutableStateOf<String?>(null) }
    var precoError by remember { mutableStateOf<String?>(null) }

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
                    text = "Criar Nova Viagem",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            item {
                OutlinedTextField(
                    value = origem,
                    onValueChange = {},
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
                    onValueChange = {},
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
                // Botão para criar viagem
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


                            // Detalhes da viagem para o QR Code
                            val tripDetails = "Origem: $origem\nDestino: $destino\nPartida: $partida\nChegada: $chegada\n" +
                                    "Preco: $preco\nRegras: $regras\nCarro: ${userDetails?.car?.Marca ?: "Desconhecido"} \n" + {userDetails?.car?.Modelo ?: "Desconhecido"} +
                                    "Matricula: ${userDetails?.car?.Matricula ?: "Desconhecido"}\n"
                            qrCodeBitmap = generateQRCode(tripDetails)

                            val qrCodeBase64 = qrCodeBitmap?.let{ bitmapToBase64(it) }
                            val carLugares = userDetails?.car?.lugares!!

                            val trip = Trip(
                                Origem = origem,
                                Destino = destino,
                                Regras = regras,
                                HoraPartida = partida,
                                HoraChegada = chegada,
                                lugaresDisponiveis = carLugares,
                                qrCode = qrCodeBase64
                            )

                            coroutineScope.launch {
                                val tripId = tripRepository.insert(trip)

                                val userTrip = UserTrip(
                                    userId = userId,
                                    tripId = tripId,
                                    role = Role.CONDUTOR
                                )
                                tripRepository.insertUserTrip(userTrip)

                                Toast.makeText(context, "Viagem criada e salva com sucesso!", Toast.LENGTH_SHORT).show()
                            }


                            if (qrCodeBitmap != null) {
                                showPopup = true
                            } else {
                                Toast.makeText(context, "Erro ao gerar QR Code!", Toast.LENGTH_SHORT).show()
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
                        text = "Criar Viagem",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Popup de confirmação com QR Code
            item {
                if (showPopup && qrCodeBitmap != null) {
                    AlertDialog(
                        onDismissRequest = { showPopup = false },
                        title = {
                            Text(
                                text = "Viagem Criada com Sucesso!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    bitmap = qrCodeBitmap!!.asImageBitmap(),
                                    contentDescription = "QR Code da Viagem",
                                    modifier = Modifier.size(200.dp)
                                )
                                Text(
                                    text = "Partilhe o QR Code com os interessados!",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showPopup = false
                                    navController.popBackStack()
                                }
                            ) {
                                Text("Concluir")
                            }
                        }
                    )
                }
            }
        }
    }
}

// Função para gerar QR Code
fun generateQRCode(text: String): Bitmap? {
    val size = 512 // Define o tamanho do QR code em pixels
    val hints = hashMapOf(EncodeHintType.MARGIN to 1) // Margem do QR code

    return try {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

@Composable
@Preview(showBackground = true)
fun PreviewCreateTripScreen() {
    val userRepository = UserRepository(FakeUserDao())
    val tripRepository = TripRepository(FakeTripDao())
    CreateTripScreen(navController = rememberNavController(),
        tripRepository = tripRepository,
        userRepository = userRepository,
        userId = 1)
}

