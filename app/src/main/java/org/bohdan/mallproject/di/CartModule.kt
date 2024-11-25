package org.bohdan.mallproject.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bohdan.mallproject.data.CartRepositoryImpl
import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): CartRepository {
        return CartRepositoryImpl(auth, firestore)
    }
}

