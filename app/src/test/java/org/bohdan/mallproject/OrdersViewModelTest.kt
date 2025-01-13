package org.bohdan.mallproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.usecase.order.GetOrdersFromFirestoreUseCase
import org.bohdan.mallproject.presentation.viewmodel.orders.OrdersViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class OrdersViewModelTest {

    // Цей rule дозволяє працювати з LiveData на головному потоці в тестах
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getOrdersFromFirestoreUseCase: GetOrdersFromFirestoreUseCase  // Мок для UseCase

    private lateinit var viewModel: OrdersViewModel  // Тестований ViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)  // Ініціалізація моків
        viewModel = OrdersViewModel(getOrdersFromFirestoreUseCase)  // Ініціалізація ViewModel
    }

    @Test
    fun `loadOrders should post error message when failed`() = runBlockingTest {
        // Мокуємо виключення
        val errorMessage = "Failed to load orders"
        Mockito.`when`(getOrdersFromFirestoreUseCase()).thenThrow(RuntimeException(errorMessage))  // Заміна на RuntimeException

        // Мок для спостерігача (Observer)
        val errorObserver = mock(Observer::class.java) as Observer<String>
        viewModel.error.observeForever(errorObserver)

        // Викликаємо метод завантаження
        viewModel.loadOrders()

        // Перевірка, чи була викликана функція onChanged з помилкою
        verify(errorObserver).onChanged(errorMessage)
        assertNull(viewModel.orders.value)
    }
}
