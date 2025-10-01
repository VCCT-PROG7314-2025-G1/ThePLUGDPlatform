package com.example.plugd.data.localRoom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.plugd.data.localRoom.entity.UserEntity

// Local database queries (RoomDB)
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY userId DESC LIMIT 1")
    suspend fun getLastLoggedInUser(): UserEntity?

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("UPDATE users SET bio = :bio, location = :location WHERE userId = :userId")
    suspend fun updateProfile(userId: String, bio: String?, location: String?)

    @Query("UPDATE users SET followersCount = :count WHERE userId = :userId")
    suspend fun updateFollowers(userId: String, count: Int)
}