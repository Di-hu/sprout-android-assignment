package com.example.myapplication.data.repository

import com.example.myapplication.data.local.UserProgressDao
import com.example.myapplication.data.model.Reward
import com.example.myapplication.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

class ProgressRepository(private val dao: UserProgressDao) {
    val userProgress: Flow<UserProgress?> = dao.getUserProgress()
    val allRewards: Flow<List<Reward>> = dao.getRewards()

    suspend fun addStars(count: Int) {
        dao.addStars(count)
    }

    suspend fun activityCompleted() {
        dao.incrementActivities()
    }

    suspend fun addReward(name: String, type: String) {
        dao.addReward(Reward(name = name, type = type))
    }
    
    suspend fun initializeProgress() {
        // Create initial progress if not exists
        dao.insertInitialProgress(UserProgress())
    }
}
