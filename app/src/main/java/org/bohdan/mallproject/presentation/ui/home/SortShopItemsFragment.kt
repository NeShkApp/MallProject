package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.SortBy

class SortShopItemsFragment(
    private val listener: SortOptionListener,
    private val selectedSortOption: SortBy?
    ) : BottomSheetDialogFragment() {

    interface SortOptionListener {
        fun onSortOptionSelected(sortOption: SortBy)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sort_shop_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sortOptionsRadioGroup: RadioGroup = view.findViewById(R.id.rg_sort_options)
        val applyButton: Button = view.findViewById(R.id.button_apply_sort)

        when (selectedSortOption) {
            SortBy.PRICE_ASC -> sortOptionsRadioGroup.check(R.id.rb_sort_price_ascending)
            SortBy.PRICE_DESC -> sortOptionsRadioGroup.check(R.id.rb_sort_price_descending)
            SortBy.RATING -> sortOptionsRadioGroup.check(R.id.rb_sort_rating)
            null -> {}
        }

        applyButton.setOnClickListener {
            val selectedOption = when (sortOptionsRadioGroup.checkedRadioButtonId) {
                R.id.rb_sort_price_ascending -> SortBy.PRICE_ASC
                R.id.rb_sort_price_descending -> SortBy.PRICE_DESC
                else -> SortBy.RATING
            }
            listener.onSortOptionSelected(selectedOption)
            dismiss()
        }

    }
}