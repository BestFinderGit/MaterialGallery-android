package com.numero.material_gallery

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.DynamicColors
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestUpdateFlow
import com.numero.material_gallery.core.isDarkTheme
import com.numero.material_gallery.core.launchWhenStartedIn
import com.numero.material_gallery.databinding.ActivityMainBinding
import com.numero.material_gallery.repository.ConfigRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    private val hideAppBarDestinationIds = setOf(
        R.id.NavigationDrawerScreen,
        R.id.CollapsingScreen,
        R.id.ComponentListScreen,
        R.id.StudiesScreen,
        R.id.SettingsScreen,

        R.id.CraneScreen,
        R.id.ReplyScreen,
        R.id.ShrineScreen
    )

    private val rootNavigationDestinationIds = setOf(
        R.id.ComponentListScreen,
        R.id.StudiesScreen,
        R.id.SettingsScreen
    )

    private val studiesDestinationIds = setOf(
        R.id.CraneScreen,
        R.id.ReplyScreen,
        R.id.ShrineScreen
    )

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(configRepository.currentTheme)
        installSplashScreen()
        if (DynamicColors.isDynamicColorAvailable()) {
            DynamicColors.applyIfAvailable(this)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        val navController = checkNotNull(
            supportFragmentManager.findFragmentById(R.id.container)
        ).findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemReselectedListener { }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isHideAppBar = hideAppBarDestinationIds.contains(destination.id)
            if (destination.id != R.id.ThemeInfoDialog) {
                if (isHideAppBar) {
                    supportActionBar?.hide()
                } else {
                    supportActionBar?.show()
                }
            }

            val isRootDestination = rootNavigationDestinationIds.contains(destination.id)
            binding.bottomNavigation.isVisible = isRootDestination

            val isStudiesDestination = studiesDestinationIds.contains(destination.id)
            val windowInsetController = WindowInsetsControllerCompat(window, window.decorView)
            windowInsetController.isAppearanceLightStatusBars = !isStudiesDestination && !isDarkTheme
        }

        configRepository.changedThemeEvent.onEach {
            recreate()
        }.launchWhenStartedIn(lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
    }

    override fun onSupportNavigateUp() = checkNotNull(
        supportFragmentManager.findFragmentById(R.id.container)
    ).findNavController().navigateUp()

    private fun checkUpdate() {
        appUpdateManager.requestUpdateFlow()
            .onEach { appUpdate ->
                when (appUpdate) {
                    is AppUpdateResult.Available -> {
                        binding.bottomNavigation.getOrCreateBadge(R.id.navSettings)
                    }
                    else -> {
                        binding.bottomNavigation.removeBadge(R.id.navSettings)
                    }
                }
            }
            .launchWhenStartedIn(lifecycleScope)
    }
}
