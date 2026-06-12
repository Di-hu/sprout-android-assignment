package com.example.myapplication.data.local

import androidx.room.*
import com.example.myapplication.data.model.Reward
import com.example.myapplication.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgress?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialProgress(progress: UserProgress)

    @Query("UPDATE user_progress SET stars = stars + :count WHERE id = 1")
    suspend fun addStars(count: Int)

    @Query("UPDATE user_progress SET activitiesCompleted = activitiesCompleted + 1 WHERE id = 1")
    suspend fun incrementActivities()

    @Query("SELECT * FROM rewards ORDER BY earnedDate DESC")
    fun getRewards(): Flow<List<Reward>>

    @Insert
    suspend fun addReward(reward: Reward)
    
    @Query("DELETE FROM user_progress")
    suspend fun clearProgress()
    
    @Query("DELETE FROM rewards")
    suspend fun clearRewards()
}