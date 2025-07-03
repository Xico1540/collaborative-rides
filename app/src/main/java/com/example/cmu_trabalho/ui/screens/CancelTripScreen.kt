package com.example.cmu_trabalho.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CancelTripScreen(onCancelConfirm: () -> Unit, onCancelDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onCancelDismiss() },
        title = { Text("Cancelar Viagem") },
        text = { Text("Tem certeza que deseja cancelar sua participação nesta viagem?") },
        confirmButton = {
            TextButton(onClick = { onCancelConfirm() }) {
                Text("Sim")
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancelDismiss() }) {
                Text("Não")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CancelTripScreenPreview() {
    CancelTripScreen(onCancelConfirm = {}, onCancelDismiss = {})
}
