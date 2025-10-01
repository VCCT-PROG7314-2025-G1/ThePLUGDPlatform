package com.example.plugd.repository

import com.example.plugd.data.localRoom.dao.UserDao
import com.example.plugd.data.localRoom.entity.UserEntity
import com.example.plugd.data.remoteFireStore.UserRemoteDataSource
import com.example.plugd.data.mappers.toUserEntity
import com.example.plugd.data.mappers.toUserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRepository(
    private val userDao: UserDao,
    private val remote: UserRemoteDataSource
) {
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun getCurrentUser(): UserEntity? {
        val local = userDao.getLastLoggedInUser()
        if (local != null) _currentUser.value = local
        return local
    }

    suspend fun getUserById(uid: String): UserEntity? {
        return userDao.getUserById(uid)
    }

    /**
     * 🔹 Fetch latest user from Firestore and save to Room
     */
    suspend fun fetchUserFromRemote(userId: String): UserEntity? {
        return try {
            val remoteUser = remote.getUser(userId)
            remoteUser?.let {
                val entity = it.toUserEntity()
                userDao.insertUser(entity) // save locally
                _currentUser.value = entity
                entity
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun loadUserFromRoom(userId: String): UserEntity? {
        val local = userDao.getUserById(userId)
        _currentUser.value = local
        return local
    }

    /**
     * 🔹 Listen for live updates from Firestore and keep Room in sync
     */
    fun observeUser(userId: String) {
        remote.observeUser(userId) { remoteUser ->
            remoteUser?.let {
                val entity = it.toUserEntity()
                scope.launch {
                    userDao.insertUser(entity)
                    _currentUser.value = entity
                }
            }
        }
    }

    /**
     * 🔹 Update user both locally and remotely
     */
    suspend fun updateUserProfile(user: UserEntity): UserEntity {
        userDao.insertUser(user) // update Room
        remote.updateUser(user.toUserProfile()) // update Firestore
        _currentUser.value = user
        return user
    }

    suspend fun logout() {
        _currentUser.value = null
    }
}
