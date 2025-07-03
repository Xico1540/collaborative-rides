package com.example.cmu_trabalho.repository

import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.database.UserDao
import com.example.cmu_trabalho.models.Car

class UserRepository(private val userDao: UserDao) {

    // Função para inserir um novo utilizador na base de dados
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    // Função para verificar se um utilizador com o mesmo email já existe
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
}
