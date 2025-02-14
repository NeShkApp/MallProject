//package org.bohdan.mallproject
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Observer
//import junit.framework.TestCase.assertNull
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.runBlockingTest
//import org.bohdan.mallproject.domain.model.Order
//import org.bohdan.mallproject.domain.usecase.order.GetOrdersFromFirestoreUseCase
//import org.bohdan.mallproject.presentation.viewmodel.orders.OrdersViewModel
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.verify
//import org.mockito.MockitoAnnotations
//
//@ExperimentalCoroutinesApi
//class OrdersViewModelTest {
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    lateinit var getOrdersFromFirestoreUseCase: GetOrdersFromFirestoreUseCase
//
//    private lateinit var viewModel: OrdersViewModel
//    private lateinit var errorObserver: Observer<String>  // Для кожного тесту окремий Observer
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.openMocks(this)
//        viewModel = OrdersViewModel(getOrdersFromFirestoreUseCase)
//    }
//
//    @After
//    fun tearDown() {
//        // Очищення Observer після кожного тесту
//        viewModel.error.removeObserver(errorObserver)
//    }
//
//    @Test
//    fun `loadOrders should post error message when failed`() = runBlockingTest {
//        val errorMessage = "Failed to load orders"
//        Mockito.`when`(getOrdersFromFirestoreUseCase()).thenThrow(RuntimeException(errorMessage))
//
//        // Створюємо мокований Observer для цього тесту
//        errorObserver = mock(Observer::class.java) as Observer<String>
//        viewModel.error.observeForever(errorObserver)
//
//        // Викликаємо метод завантаження
//        viewModel.loadOrders()
//
//        // Перевірка, чи викликана функція onChanged з помилкою
//        verify(errorObserver).onChanged(errorMessage)
//        assertNull(viewModel.orders.value)
//    }
//}