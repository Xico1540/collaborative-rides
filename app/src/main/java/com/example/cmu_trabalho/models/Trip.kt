package com.example.cmu_trabalho.models

import android.text.format.DateFormat
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "trip_table")
data class Trip(
    @PrimaryKey(autoGenerate = true) val tripId: Long = 0,
    val Origem: String,
    val Destino: String,
    val Regras: String,
    val HoraPartida: String,
    val HoraChegada: String,
    val lugaresDisponiveis: Int,
    val qrCode: String? = null
)

/*
@Entity(
    tableName = "stoppingPoints_table",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StoppingPoints(
    @PrimaryKey(autoGenerate = true) val stoppingPointId: Long = 0,
    val tripId: Long,
    val PontoParagem: String,
    val preçoPassageiro: Double,
    val Hora: String
)

data class TripWithStoppingPoints(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "tripId"
    )
    val stoppingPoints: List<StoppingPoints>
)

data class TripWithUsers(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "tripId",
        associateBy = Junction(UserTrip::class)
    )
    val users: List<UserWithRole>
)

data class UserWithRole(
    @Embedded val user: User,
    val role: String
)
*/