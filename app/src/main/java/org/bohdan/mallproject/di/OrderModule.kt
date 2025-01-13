package org.bohdan.mallproject.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bohdan.mallproject.data.repositoryimpl.OrderRepositoryImpl
import org.bohdan.mallproject.domain.repository.OrderRepository
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {

    @Provides
    @Singleton
    fun provideOrderRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        shopItemDetailsRepository: ShopItemDetailsRepository
    ): OrderRepository {
        return OrderRepositoryImpl(
            auth,
            firestore,
            shopItemDetailsRepository
        )
    }
}
