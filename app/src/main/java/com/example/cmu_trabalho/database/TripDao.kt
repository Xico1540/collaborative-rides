package com.example.cmu_trabalho.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Query
import androidx.room.Update
import com.example.cmu_trabalho.models.*
import com.example.cmu_trabalho.models.enums.Role

@Dao
interface TripDao {

    // Inserir uma nova viagem
    @Insert
    suspend fun insert(trip: Trip): Long

    // Obter uma viagem pelo ID
    @Query("SELECT * FROM trip_table WHERE tripId = :tripId")
    suspend fun getTripById(tripId: Long): Trip?

    // Inserir uma nova associação de user e trip
    @Insert
    suspend fun insertUserTrip(userTrip: UserTrip)
}

class FakeTripDao : TripDao {
    override suspend fun insert(trip: Trip): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getTripById(tripId: Long): Trip? {
        TODO("Not yet implemented")
    }

    override suspend fun insertUserTrip(userTrip: UserTrip) {
        TODO("Not yet implemented")
    }
}

/*
// Inserir novos pontos de paragem na viagem
@Insert
suspend fun insertStoppingPoints(stoppingPoints: List<StoppingPoints>)

// Consultar uma viagem com todos os seus pontos de paragem
@Transaction
@Query("SELECT * FROM trip_table WHERE tripId = :tripId")
suspend fun getTripWithStoppingPoints(tripId: Long): TripWithStoppingPoints?

// Consultar uma viagem com todos os seus utilizadores e os seus papéis
@Transaction
@Query("SELECT * FROM trip_table WHERE tripId = :tripId")
suspend fun getTripWithUsers(tripId: Long): TripWithUsers?

// Obter todos os utilizadores que são passageiros numa viagem
@Transaction
@Query("SELECT * FROM user_trip_table WHERE tripId = :tripId AND role = 'PASSAGEIRO'")
suspend fun getPassengersInTrip(tripId: Long): List<UserWithRole>

// Obter o condutor de uma viagem
@Transaction
@Query("SELECT * FROM user_trip_table WHERE tripId = :tripId AND role = 'CONDUTOR'")
suspend fun getDriverInTrip(tripId: Long): UserWithRole

// Método para associar um usuário a uma viagem
@Transaction
suspend fun addUserToTrip(user: User, tripId: Long) {
   val trip = getTripById(tripId)
   if (trip != null) {
       // Verificar se há lugares disponíveis
       if (trip.lugaresDisponiveis > 0) {
           // Se houver lugares disponíveis, associar o utilizador à viagem como PASSAGEIRO
           val userTrip = UserTrip(userId = user.userId, tripId = tripId, role = Role.PASSAGEIRO)
           insertUserTrip(userTrip)


           // Atualizar os lugares disponíveis na viagem (decrementar 1)
           val updatedTrip = trip.copy(lugaresDisponiveis = trip.lugaresDisponiveis - 1)
           updateTrip(updatedTrip)
       } else {
           throw IllegalStateException("Não há lugares disponíveis para adicionar este usuário.")
       }
   } else {
       throw IllegalArgumentException("Viagem não encontrada.")
   }
}

// Método para adicionar novos pontos de paragem à viagem
@Transaction
suspend fun addStoppingPointsToTrip(stoppingPoints: List<StoppingPoints>, tripId: Long) {
   // Verificar se a viagem existe antes de adicionar os pontos de paragem
   val trip = getTripById(tripId)
   if (trip != null) {
       // Se a viagem existe, adicionar os pontos de paragem
       insertStoppingPoints(stoppingPoints)
   } else {
       throw IllegalArgumentException("Viagem não encontrada.")
   }
}

// Atualizar informações de uma viagem (como lugares disponíveis)
@Update
suspend fun updateTrip(trip: Trip)
}
*/