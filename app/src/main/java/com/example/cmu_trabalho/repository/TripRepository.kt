package com.example.cmu_trabalho.repository

import com.example.cmu_trabalho.models.Trip
import com.example.cmu_trabalho.database.TripDao
import com.example.cmu_trabalho.models.UserTrip

class TripRepository(private val tripDao: TripDao) {

    suspend fun insert(trip: Trip): Long {
        return tripDao.insert(trip)
    }

    suspend fun insertUserTrip(userTrip: UserTrip) {
        tripDao.insertUserTrip(userTrip)
    }

}
