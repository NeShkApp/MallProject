package org.bohdan.mallproject.presentation.ui.home

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory

class FilterShopItemsFragment(
    private val listener: FilterOptionListener,
) : BottomSheetDialogFragment() {

    interface FilterOptionListener {
        fun onFilterOptionSelected(category: Category?, subcategory: Subcategory?)
    }
}
