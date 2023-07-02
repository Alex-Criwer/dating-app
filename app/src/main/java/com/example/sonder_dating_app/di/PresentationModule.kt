package com.example.sonder_dating_app.di


import android.content.Context
import com.example.sonder_dating_app.firebase.FirebaseAuthProvider
import com.example.sonder_dating_app.firebase.interactor.FirebaseAuthInteractor
import com.example.sonder_dating_app.firebase.interactor.FirebaseAuthInteractorImpl
import com.example.sonder_dating_app.location_manager.LocationManager
import com.example.sonder_dating_app.location_manager.LocationManagerImpl
import com.example.sonder_dating_app.service.video_call.VideoCallService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseMessaging() : FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideAuthenticationProvider(firebaseAuth: FirebaseAuth, firebaseMessaging: FirebaseMessaging) =
        FirebaseAuthProvider(firebaseAuth, firebaseMessaging)

    @Provides
    @Singleton
    fun provideAuthenticationInteractor(auth: FirebaseAuthProvider) : FirebaseAuthInteractor {
        return FirebaseAuthInteractorImpl(auth)
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context) : LocationManager {
        return LocationManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideVideoCallService() = VideoCallService()
}
