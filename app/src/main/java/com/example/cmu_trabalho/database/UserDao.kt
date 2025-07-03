package com.example.cmu_trabalho.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cmu_trabalho.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?


    @Query("SELECT * FROM user_table WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): User?
}

class FakeUserDao : UserDao {
    private val users = mutableListOf<User>()

    override suspend fun insert(user: User) {
        users.add(user)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    override suspend fun getUserById(userId: Long): User? {
        return users.find { it.userId == userId }
    }
}


