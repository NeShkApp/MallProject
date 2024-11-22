package org.bohdan.mallproject.presentation.viewmodel.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory

class AllProductsViewModelFactory(
    private val application: Application,
    private val category: Category?,
    private val subcategory: Subcategory?,
    private val searchQuery: String?
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AllProductsViewModel::class.java)){
            return AllProductsViewModel(application, category, subcategory, searchQuery) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}