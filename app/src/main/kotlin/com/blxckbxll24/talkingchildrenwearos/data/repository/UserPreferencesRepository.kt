package com.blxckbxll24.talkingchildrenwearos.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.blxckbxll24.talkingchildrenwearos.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(
    context: Context
) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    
    private val _userPreferences = MutableStateFlow(getUserPreferences())
    val userPreferences: Flow<UserPreferences> = _userPreferences.asStateFlow()
    
    private fun getUserPreferences(): UserPreferences {
        return UserPreferences(
            enableNotifications = sharedPreferences.getBoolean(KEY_ENABLE_NOTIFICATIONS, true),
            autoHeartRateMonitoring = sharedPreferences.getBoolean(KEY_AUTO_HEART_RATE, false),
            syncWithCompanion = sharedPreferences.getBoolean(KEY_SYNC_COMPANION, true),
            heartRateThresholdHigh = sharedPreferences.getInt(KEY_HR_THRESHOLD_HIGH, 140),
            heartRateThresholdLow = sharedPreferences.getInt(KEY_HR_THRESHOLD_LOW, 60),
            dailyStepGoal = sharedPreferences.getInt(KEY_DAILY_STEP_GOAL, 10000)
        )
    }
    
    suspend fun updateEnableNotifications(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ENABLE_NOTIFICATIONS, enabled).apply()
        _userPreferences.value = getUserPreferences()
    }
    
    suspend fun updateAutoHeartRateMonitoring(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_AUTO_HEART_RATE, enabled).apply()
        _userPreferences.value = getUserPreferences()
    }
    
    suspend fun updateSyncWithCompanion(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_SYNC_COMPANION, enabled).apply()
        _userPreferences.value = getUserPreferences()
    }
    
    suspend fun updateHeartRateThresholds(high: Int, low: Int) {
        sharedPreferences.edit()
            .putInt(KEY_HR_THRESHOLD_HIGH, high)
            .putInt(KEY_HR_THRESHOLD_LOW, low)
            .apply()
        _userPreferences.value = getUserPreferences()
    }
    
    suspend fun updateDailyStepGoal(goal: Int) {
        sharedPreferences.edit().putInt(KEY_DAILY_STEP_GOAL, goal).apply()
        _userPreferences.value = getUserPreferences()
    }
    
    companion object {
        private const val PREFERENCES_NAME = "talking_children_preferences"
        private const val KEY_ENABLE_NOTIFICATIONS = "enable_notifications"
        private const val KEY_AUTO_HEART_RATE = "auto_heart_rate"
        private const val KEY_SYNC_COMPANION = "sync_companion"
        private const val KEY_HR_THRESHOLD_HIGH = "hr_threshold_high"
        private const val KEY_HR_THRESHOLD_LOW = "hr_threshold_low"
        private const val KEY_DAILY_STEP_GOAL = "daily_step_goal"
    }
}