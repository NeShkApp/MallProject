package org.bohdan.mallproject.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bohdan.mallproject.data.FavoriteRepositoryImpl
import org.bohdan.mallproject.domain.repository.FavoriteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteModule {

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FavoriteRepository{
        return FavoriteRepositoryImpl(
            auth,
            firestore
        )
    }
}