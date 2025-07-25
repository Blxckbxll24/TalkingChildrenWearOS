package com.blxckbxll24.talkingchildrenwearos

import android.app.Application

class TalkingChildrenApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // WorkManager se inicializa automáticamente
        // No necesitamos hacer nada aquí para WorkManager
    }
}