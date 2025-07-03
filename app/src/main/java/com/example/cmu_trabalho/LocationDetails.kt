package com.example.cmu_trabalho

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapboxApi {
    @GET("geocoding/v5/mapbox.places/{longitude},{latitude}.json")
    suspend fun getReverseGeocoding(
        @Path("longitude") longitude: Double,
        @Path("latitude") latitude: Double,
        @Query("access_token") accessToken: String
    ): MapboxResponse
}

data class MapboxResponse(
    val features: List<Feature>
)

data class Feature(
    val place_name: String,
    val context: List<Contexto>
)

data class Contexto(
    val id: String,
    val text: String
)

// Função que usa a API do Mapbox para obter detalhes da localização a partir das coordenadas
suspend fun getLocationDetailsFromCoordinates(latitude: Double, longitude: Double, context: Context): String? {
    val mapboxAccessToken = context.getString(R.string.mapbox_access_token)

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.mapbox.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val mapboxApi = retrofit.create(MapboxApi::class.java)

    return try {
        val response = mapboxApi.getReverseGeocoding(longitude, latitude, mapboxAccessToken)
        println("API Response: $response")

        // Retorna o primeiro place_name encontrado, ou "Desconhecido" se não houver
        return response.features.firstOrNull()?.place_name ?: "Desconhecido"
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


