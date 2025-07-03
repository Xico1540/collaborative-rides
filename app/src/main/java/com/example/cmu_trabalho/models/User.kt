package com.example.cmu_trabalho.models

import android.text.format.DateFormat
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val pais: String,
    val birthDate: String,
    @Embedded
    val car: Car? = null
)

data class Car(
    val Marca: String,
    val Modelo: String,
    val lugares: Int,
    val Matricula: String
)

