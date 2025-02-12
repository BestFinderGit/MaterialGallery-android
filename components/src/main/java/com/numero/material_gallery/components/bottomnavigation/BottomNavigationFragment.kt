package com.numero.material_gallery.components.bottomnavigation

import android.os.Bundle
import android.view.View
import com.google.android.material.navigation.NavigationBarView
import com.numero.material_gallery.components.ComponentFragment
import com.numero.material_gallery.components.R
import com.numero.material_gallery.components.databinding.FragmentBottomNavigationBinding
import com.numero.material_gallery.core.delegate.viewBinding
import dev.chrisbanes.insetter.applyInsetter

class BottomNavigationFragment : ComponentFragment(R.layout.fragment_bottom_navigation) {

    private val binding by viewBinding { FragmentBottomNavigationBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        binding.scrollView.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
        }
    }

    private fun initViews() {
        val listener = NavigationBarView.OnItemSelectedListener {
            binding.bottomNavigation.setCheckedItem(it.itemId, true)
            false
        }
        binding.bottomNavigation.setOnItemSelectedListener(listener)

        binding.removeItemButton.setOnClickListener {
            updateBottomNavigationItemCount(MenuItemAction.REMOVE)
        }
        binding.addItemButton.setOnClickListener {
            updateBottomNavigationItemCount(MenuItemAction.ADD)
        }

        binding.withBadgeBottomNavigation.apply {
            getOrCreateBadge(R.id.navigation_item_1)
            getOrCreateBadge(R.id.navigation_item_2).apply {
                number = 10
            }
            getOrCreateBadge(R.id.navigation_item_3).apply {
                number = 1000
            }
        }
    }

    private fun updateBottomNavigationItemCount(action: MenuItemAction) {
        val currentItemCount = binding.bottomNavigation.visibleItemCount
        when (action) {
            MenuItemAction.ADD -> {
                binding.bottomNavigation.setVisibleItem(currentItemCount, true)
                binding.removeItemButton.isEnabled = true
                binding.addItemButton.isEnabled = currentItemCount + 1 < MAX_ITEM_COUNT
            }
            MenuItemAction.REMOVE -> {
                binding.bottomNavigation.setVisibleItem(currentItemCount - 1, false)
                binding.removeItemButton.isEnabled = currentItemCount - 1 > MIN_ITEM_COUNT
                binding.addItemButton.isEnabled = true
            }
        }
    }

    private enum class MenuItemAction {
        ADD, REMOVE
    }

    companion object {
        private const val MIN_ITEM_COUNT = 3
        private const val MAX_ITEM_COUNT = 5
    }
}