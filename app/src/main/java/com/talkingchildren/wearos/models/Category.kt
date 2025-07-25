package com.talkingchildren.wearos.models

/**
 * Data class representing a category of messages
 */
data class Category(
    val id: String,
    val name: String,
    val iconResource: Int,
    val colorResource: Int = android.R.color.transparent
) {
    companion object {
        const val BASIC = "basic"
        const val EMOTIONS = "emotions"
        const val NEEDS = "needs"
    }
}