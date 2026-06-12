package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val stars: Int = 0,
    val activitiesCompleted: Int = 0,
    val screenTimeMinutes: Long = 0,
    val lastActive: Long = System.currentTimeMillis()
)

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // Badge, Sticker, etc.
    val earnedDate: Long = System.currentTimeMillis()
)