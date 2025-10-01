package com.example.plugd.data.mappers

import com.example.plugd.data.localRoom.entity.UserEntity
import com.example.plugd.model.UserProfile

// Convert Room entity to UI model
fun UserEntity.toUserProfile(): UserProfile = UserProfile(
    userId = userId,
    name = name,
    username = username,
    email = email,
    role = role,
    phone = phone,
    bio = bio,
    gender = gender,
    location = location,
    followersCount = followersCount
)

// Convert UI model back to Room entity
fun UserProfile.toUserEntity(password: String? = null): UserEntity = UserEntity(
    userId = userId,
    name = name,
    username = username,
    email = email,
    password = password,
    phone = phone,
    role = role,
    bio = bio,
    gender = gender,
    location = location,
    followersCount = followersCount
)