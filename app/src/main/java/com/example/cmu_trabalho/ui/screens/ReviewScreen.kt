package com.example.cmu_trabalho.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.cmu_trabalho.models.Car
import com.example.cmu_trabalho.models.User

data class Review(
    val userName: String,
    val rating: Int,
    val comment: String
)


@Composable
fun ViewReviewsScreen() {
    val reviews = listOf(
        Review("João", 5, "Excelente viagem! Recomendo."),
        Review("Maria", 4, "Boa viagem, mas poderia ser mais confortável."),
        Review("Pedro", 3, "Viagem ok, mas teve alguns imprevistos.")
    )

    // Título da página
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Avaliações da Viagem", style = MaterialTheme.typography.headlineMedium)

        // Exibir a lista de avaliações
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(reviews.size) { index ->
                val review = reviews[index]
                ReviewItem(review)
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = review.userName, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            for (star in 1..5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (star <= review.rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItemPreview() {
    ReviewItem(review = Review(userName = "Miguel", rating = 4, comment = "Boa viagem!"))
}

@Preview(showBackground = true)
@Composable
fun ViewReviewsScreenPreview() {
    ViewReviewsScreen()
}
