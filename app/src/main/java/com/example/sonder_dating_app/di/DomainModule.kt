package com.example.sonder_dating_app.di

import android.content.Context
import com.example.sonder_domain.GrpcService
import com.example.sonder_domain.repository.SonderMessageTokenSharedPref
import com.example.sonder_domain.repository.SonderRemoteDataSource
import com.example.sonder_domain.repository.SonderRepository
import com.example.sonder_domain.repository.SonderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideGrpcService() : GrpcService = GrpcService("130.193.51.233", 20000)

    @Provides
    @Singleton
    fun provideSonderRemoteDataSource(grpcService: GrpcService): SonderRemoteDataSource {
        return SonderRemoteDataSource(grpcService)
    }

    @Provides
    @Singleton
    fun provideSonderMessageTokenSharedPref(@ApplicationContext context: Context): SonderMessageTokenSharedPref {
        return SonderMessageTokenSharedPref(context)
    }

    @Provides
    @Singleton
    fun provideSonderRepository(
        sonderRemoteDataSource: SonderRemoteDataSource,
        sonderMessageTokenSharedPref: SonderMessageTokenSharedPref
    ) : SonderRepository {
        return SonderRepositoryImpl(sonderRemoteDataSource, sonderMessageTokenSharedPref)
    }
}
