package com.numero.material_gallery.components.appbar.bottom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.numero.material_gallery.R
import com.numero.material_gallery.fragment.ThemeInfoBottomSheetDialog
import com.numero.material_gallery.repository.ConfigRepository
import kotlinx.android.synthetic.main.activity_bottom_app_bar.*
import org.koin.android.ext.android.inject

class BottomAppBarActivity : AppCompatActivity(R.layout.activity_bottom_app_bar) {

    private val configRepository by inject<ConfigRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(configRepository.themeRes)
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        fab.setOnClickListener {
            Toast.makeText(this@BottomAppBarActivity, "Clicked FAB", Toast.LENGTH_SHORT).show()
        }

        fabPositionRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.attachedCenterRadioButton -> bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                R.id.attachedEndRadioButton -> bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            }
        }

        fabAnimationRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.scaleRadioButton -> bottomAppBar.fabAnimationMode = BottomAppBar.FAB_ANIMATION_MODE_SCALE
                R.id.slideRadioButton -> bottomAppBar.fabAnimationMode = BottomAppBar.FAB_ANIMATION_MODE_SLIDE
            }
        }

        fabVisiblyRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.showRadioButton -> fab.show()
                R.id.hideRadioButton -> fab.hide()
            }
        }

        bottomAppBar.replaceMenu(R.menu.bottom_app_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_common, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                ThemeInfoBottomSheetDialog.newInstance().showIfNeeded(supportFragmentManager)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, BottomAppBarActivity::class.java)
    }
}