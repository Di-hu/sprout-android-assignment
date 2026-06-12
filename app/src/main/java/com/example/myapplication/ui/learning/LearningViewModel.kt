package com.example.myapplication.ui.learning

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.ProgressRepository
import kotlinx.coroutines.launch

class LearningViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProgressRepository

    private val _rewardEarned = MutableLiveData<Int?>()
    val rewardEarned: LiveData<Int?> = _rewardEarned

    init {
        val dao = AppDatabase.getDatabase(application).userProgressDao()
        repository = ProgressRepository(dao)
    }

    fun onLetterLearned(letter: String) {
        viewModelScope.launch {
            val coins = 5
            repository.addStars(coins)
            // No activity completion popup for every single item anymore
            // repository.activityCompleted() 
            // _rewardEarned.postValue(coins)
        }
    }
    
    fun onActivityCompleted(activityName: String) {
        viewModelScope.launch {
            val coins = 20
            repository.addStars(coins)
            repository.activityCompleted()
            _rewardEarned.postValue(coins)
        }
    }

    fun onRewardShown() {
        _rewardEarned.value = null
    }
}