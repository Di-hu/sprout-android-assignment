package com.example.myapplication.ui.camera

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.ProgressRepository
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProgressRepository

    private val _rewardEarned = MutableLiveData<Int?>()
    val rewardEarned: LiveData<Int?> = _rewardEarned

    init {
        val dao = AppDatabase.getDatabase(application).userProgressDao()
        repository = ProgressRepository(dao)
    }

    fun onObjectFound(objectName: String) {
        viewModelScope.launch {
            val coins = 10
            repository.addStars(coins)
            repository.activityCompleted()
            repository.addReward("Found a $objectName!", "Explorer Badge")
            _rewardEarned.postValue(coins)
        }
    }

    fun onRewardShown() {
        _rewardEarned.value = null
    }
}