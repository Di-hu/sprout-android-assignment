package com.example.myapplication.ui.parent

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.model.Reward
import com.example.myapplication.data.model.UserProgress
import com.example.myapplication.data.repository.ProgressRepository
import kotlinx.coroutines.launch

class ParentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProgressRepository
    val userProgress: LiveData<UserProgress?>
    val allRewards: LiveData<List<Reward>>

    init {
        val dao = AppDatabase.getDatabase(application).userProgressDao()
        repository = ProgressRepository(dao)
        userProgress = repository.userProgress.asLiveData()
        allRewards = repository.allRewards.asLiveData()
        
        viewModelScope.launch {
            repository.initializeProgress()
        }
    }

    fun addStars(count: Int) {
        viewModelScope.launch {
            repository.addStars(count)
        }
    }

    fun completeActivity() {
        viewModelScope.launch {
            repository.activityCompleted()
        }
    }

    fun addReward(name: String, type: String) {
        viewModelScope.launch {
            repository.addReward(name, type)
        }
    }
}