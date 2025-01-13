package org.bohdan.mallproject.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bohdan.mallproject.data.repositoryimpl.HomeRepositoryImpl
import org.bohdan.mallproject.domain.repository.HomeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllProductsModule {

    @Provides
    @Singleton
    fun provideHomeRepository(
        db: FirebaseFirestore
    ): HomeRepository {
        return HomeRepositoryImpl(db)
    }
}