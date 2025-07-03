package pt.ipp.estg.androidintentscompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cmu_trabalho.R
import pt.ipp.estg.androidintentscompose.ui.theme.AndroidIntentsComposeTheme

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val timestamp: String,
    var isRead: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val notifications = listOf(
        Notification(1, "Reserva Confirmada", "Sua reserva para a viagem Porto -> Lisboa foi confirmada.", "15 minutos atrás"),
        Notification(2, "Nova Mensagem", "Você recebeu uma nova mensagem de um passageiro.", "30 minutos atrás"),
        Notification(3, "Alteração de Horário", "A viagem Porto -> Coimbra foi remarcada para as 15h.", "1 hora atrás"),
        Notification(4, "Avaliação Recebida", "Você recebeu uma avaliação de 4 estrelas em sua última viagem.", "2 horas atrás"),
        Notification(5, "Mensagem de Passageiro", "O passageiro 'Ana' perguntou sobre a sua viagem.", "3 horas atrás")
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopAppBar(
                title = { Text("Notificações") },
                actions = {
                    IconButton(onClick = { /* Ação para marcar todas como lidas */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Marcar como lidas")
                    }
                }
            )

            // Lista de Notificações
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                notifications.forEach { notification ->
                    NotificationItem(notification)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícone de notificação
                IconButton(onClick = { /* Marcar como lida */ }) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Notificação",
                        tint = if (notification.isRead) Color.Gray else  MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (notification.isRead) Color.Gray else Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (notification.isRead) Color.Gray else Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = notification.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (notification.isRead) Color.Gray else Color.Black
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    AndroidIntentsComposeTheme {
        NotificationsScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationReadItemPreview() {
    val notification = Notification(
        id = 1,
        title = "Reserva Confirmada",
        message = "Sua reserva para a viagem Porto -> Lisboa foi confirmada.",
        timestamp = "15 minutos atrás",
        isRead = true
    )
    NotificationItem(notification)
}

@Preview(showBackground = true)
@Composable
fun NotificationNotReadItemPreview() {
    val notification = Notification(
        id = 1,
        title = "Reserva Confirmada",
        message = "Sua reserva para a viagem Porto -> Lisboa foi confirmada.",
        timestamp = "15 minutos atrás",
        isRead = false
    )
    NotificationItem(notification)
}
