package org.bohdan.mallproject.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bohdan.mallproject.data.ShopItemDetailsRepositoryImpl
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShopItemDetailsModule {

    @Provides
    @Singleton
    fun provideShopItemDetailsRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): ShopItemDetailsRepository{
        return ShopItemDetailsRepositoryImpl(firestore, auth)
    }
}