package com.exercisechat.data

import androidx.annotation.IntRange
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    @IntRange(1, 3) val avatarId: Int = 1
) {

    val fullName: String
        get() = "$firstName $lastName"
}
