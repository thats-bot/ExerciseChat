package com.exercisechat.data.source.local

import androidx.room.*
import com.exercisechat.domain.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id == :userId")
    suspend fun get(userId: Long): User?

    @Insert
    suspend fun insert(user: User): Long

    @Transaction
    @Insert
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}
