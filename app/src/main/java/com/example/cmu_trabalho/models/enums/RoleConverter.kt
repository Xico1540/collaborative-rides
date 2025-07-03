package com.example.cmu_trabalho.models.enums
import androidx.room.TypeConverter

class RoleConverter {
    @TypeConverter
    fun fromRole(role: Role): String {
        return role.name
    }

    @TypeConverter
    fun toRole(role: String): Role {
        return Role.valueOf(role)
    }
}
