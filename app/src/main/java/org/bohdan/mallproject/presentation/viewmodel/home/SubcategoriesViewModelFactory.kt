package org.bohdan.mallproject.presentation.viewmodel.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bohdan.mallproject.domain.model.Category

class SubcategoriesViewModelFactory(
    private val category: Category,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubcategoriesViewModel::class.java)) {
            return SubcategoriesViewModel(application, category) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}