package com.example.cmu_trabalho.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cmu_trabalho.models.enums.*

@Entity(
    tableName = "user_trip_table",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"]
        ),
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(RoleConverter::class)
data class UserTrip(
    @PrimaryKey(autoGenerate = true) val userTripId: Long = 0,
    val userId: Long,
    val tripId: Long,
    val role: Role
)

