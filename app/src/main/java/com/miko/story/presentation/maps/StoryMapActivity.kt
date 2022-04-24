package com.miko.story.presentation.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityStoryMapBinding
import com.miko.story.domain.model.StoriesParam
import com.miko.story.domain.model.Story
import com.miko.story.presentation.story.StoryViewModel
import com.miko.story.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryMapActivity : BaseActivity<ActivityStoryMapBinding>(), OnMapReadyCallback {

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            enableCurrentLocation()
        } else {
            showToast(getString(R.string.error_permission_failed))
        }
    }
    private val storyViewModel: StoryViewModel by viewModel()
    private val settingPreferences: SettingPreferences by inject()

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var token: String
    private var index = 1
    private var storiesCount = 0

    override fun getViewBinding(): ActivityStoryMapBinding =
        ActivityStoryMapBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupToolbar(getString(R.string.title_maps), true)
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
    }

    override fun setupAction() {
        binding.btnLoadMore.setOnClickListener {
            index++
            getStoriesMaps()
        }
    }

    override fun setupProcess() {
        lifecycleScope.launch {
            settingPreferences.getToken().collect {
                token = it
                getStoriesMaps()
            }
        }
    }

    override fun setupObserver() {
        storyViewModel.mapsResult.observe(this,
            onLoading = {
                showLoading()
            },
            onSuccess = {
                dismissLoading()
                if (it.isNotEmpty()) {
                    setStoriesMarker(it)
                }
            },
            onError = {
                dismissLoading()
                showErrorDialog(it) {
                    setupProcess()
                }
            }
        )
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        enableCurrentLocation()
        setupMapStyle()

        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isMapToolbarEnabled = true
            isCompassEnabled = true
        }
    }

    private fun setupMapStyle() {
        try {
            val mapStyleResult = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!mapStyleResult) showToast(getString(R.string.error_parsing_map_style))
        } catch (e: Exception) {
            showToast(e.message.orEmpty())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun getStoriesMaps() {
        storyViewModel.getStoriesMaps(token, StoriesParam(true, 10, index))
    }

    private fun enableCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setStoriesMarker(stories: List<Story>) {
        storiesCount += stories.size
        val storiesCount = getString(R.string.label_stories_count, storiesCount)
        binding.tvStoriesCounter.text = storiesCount
        val storiesGrouped = stories.groupBy {
            it.latLng
        }
        storiesGrouped.keys.asIterable().forEach { latLng ->
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(storiesGrouped[latLng]?.first()?.name.orEmpty())
            )
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, StoryMapActivity::class.java).apply {

            })
        }
    }
}