package com.example.cmu_trabalho.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.example.cmu_trabalho.R


@Composable
fun DetailsTripScreen(navController: NavController, isDarkTheme: Boolean) {
    val context = LocalContext.current

    // Estado para armazenar o QR Code gerado
    var qrCodeBitmap: Bitmap? by remember { mutableStateOf(null) }
    var showPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .background(if (isDarkTheme) Color.Black else Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Seta de navegação para voltar, posicionado no topo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp), // Remove o padding extra
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black
                    )
                }
            }

            // "De" e "Para"
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Origem:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        "Porto",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Destino:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        "Lisboa",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            // "Lugares disponíveis"
            Text(
                "Lugares disponíveis: 3",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )

            Divider(color = Color.Gray, thickness = 1.dp)

            // Trajeto
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // "Trajeto" com espaçamento
                    Text(
                        "Trajeto",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    // Trajeto
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Porto
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Porto",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    ) {
                                        append("17:00")
                                    }
                                },
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }

                        Text("→", fontSize = 20.sp)

                        // Aveiro
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Aveiro",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    ) {
                                        append("17:40")
                                    }
                                },
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }

                        Text("→", fontSize = 20.sp)

                        // Coimbra
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Coimbra",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    ) {
                                        append("18:10")
                                    }
                                },
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }

                        Text("→", fontSize = 20.sp)

                        // Lisboa
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Lisboa",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    ) {
                                        append("20:00")
                                    }
                                },
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            // Secção "Condutor"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Condutor",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Nome: Bruno Ferreira",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text("País: Portugal", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
                    Text("Boleias: 29", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
                    Text(
                        buildAnnotatedString {
                            append("Rating: ★★★★☆ ")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light
                                )
                            ) {
                                append("(60)")
                            }
                        },
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                // Foto do condutor
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.estg),
                        contentDescription = "User profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            // Seção "Carro"
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Carro",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Marca: Toyota", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
                    Text("Modelo: Yaris", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
                    Text("Matrícula: AB-10-AB", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
                    Text("Capacidade: 5", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
                }
                // Imagem do carro
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Carro", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botões "Gerar QR" e "Solicitar boleia"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Gerar os dados do QR code
                        val tripData =
                            "Origem: Porto\nDestino: Lisboa\nHora: 17:00 - 20:00\nCondutor: Bruno Ferreira\nCarro: Toyota Yaris\nBoleias: 29"

                        // Gerar o código QR com base nos dados
                        qrCodeBitmap = generateQRCodeTripDetails(tripData)

                        if (qrCodeBitmap != null) {
                            showPopup = true
                        } else {
                            Toast.makeText(context, "Erro ao gerar QR Code!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Gerar QR", fontSize = 16.sp, fontFamily = FontFamily.SansSerif)
                }
            }
            // Botão de compartilhamento fora do AlertDialog
            Button(
                onClick = {
                    try {
                        val tripData =
                            "Origem: Porto, Destino: Lisboa, Hora: 17:00 - 20:00, Condutor: Bruno Ferreira, Carro: Toyota Yaris, Boleias: 29"
                        val shareableLink = "https://www.exemplo.com/boleia?details=$tripData"

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                shareableLink
                            )
                            type = "text/plain"
                        }


                        val chooserIntent = Intent.createChooser(sendIntent, "Partilhar Boleia")
                        context.startActivity(chooserIntent)
                    } catch (e: Exception) {
                        Log.e("ShareError", "Erro ao compartilhar a boleia", e)
                        Toast.makeText(
                            context,
                            "Erro ao compartilhar a boleia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Partilhar Boleia", fontSize = 16.sp, fontFamily = FontFamily.SansSerif)
            }
            if (showPopup && qrCodeBitmap != null) {
                AlertDialog(
                    onDismissRequest = { showPopup = false },
                    title = {
                        Text("QR Code da Viagem", fontSize = 18.sp)
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Mostrar o QR Code gerado
                            Image(
                                bitmap = qrCodeBitmap!!.asImageBitmap(),
                                contentDescription = "QR Code da Viagem",
                                modifier = Modifier.size(200.dp)
                            )
                            // Texto de explicação
                            Text(
                                text = "Partilhe o QR Code com os interessados!",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showPopup = false }
                        ) {
                            Text("Fechar")
                        }
                    }
                )
            }
        }
    }
}

fun generateQRCodeTripDetails(data: String): Bitmap? {
    val writer = QRCodeWriter()
    val hints = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512, hints)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(
                x,
                y,
                if (bitMatrix.get(
                        x,
                        y
                    )
                ) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            )
        }
    }
    return bitmap
}

@Composable
@Preview(showBackground = true)
fun PreviewDetailsTripScreen() {
    DetailsTripScreen(navController = NavController(LocalContext.current), false)
}
