package com.miko.story.presentation.story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityAddStoryBinding
import com.miko.story.domain.model.AddStoryParam
import com.miko.story.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {

    private val storyViewModel: StoryViewModel by viewModel()
    private val settingPreferences: SettingPreferences by inject()
    private val fusedLocationProviderClient: FusedLocationProviderClient by inject()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            imageFile?.let { file ->
                setPreviewImage(file.absolutePath)
            }
        }
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val selectedImageUri = it.data?.data as Uri
            imageFile = FileUtils.uriToFile(selectedImageUri, this)
            imageFile?.let { file ->
                setPreviewImage(file.absolutePath)
            }
        }
    }
    private var imageFile: File? = null
    private var token: String = ""
    private var location: Location? = null

    override fun getViewBinding(): ActivityAddStoryBinding =
        ActivityAddStoryBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_add_story), true)
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
    }

    override fun setupAction() {
        with(binding) {
            btnCamera.setOnClickListener {
                imageFile = FileUtils.createCustomTempImage(this@AddStoryActivity)
                val imageUri = FileProvider.getUriForFile(this@AddStoryActivity, "$packageName.provider", imageFile!!)
                cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                })
            }
            btnGallery.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_GET_CONTENT
                    type = "image/*"
                }
                galleryLauncher.launch(Intent.createChooser(intent, getString(R.string.label_choose_a_picture)))
            }
            btnUpload.setOnClickListener {
                upload()
            }
            swAddLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tvLocation.visible()
                    getCurrentLocation()
                } else {
                    tvLocation.gone()
                    location = null
                }
            }
        }
    }

    override fun setupProcess() {
        lifecycleScope.launch {
            settingPreferences.getToken().collect {
                token = it
            }
        }
    }

    override fun setupObserver() {
        storyViewModel.uploadResult.observe(this,
            onLoading = {
                showLoading()
            },
            onSuccess = {
                dismissLoading()
                StoryActivity.start(this)
            },
            onError = {
                dismissLoading()
                showErrorDialog(it) {
                    upload()
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setPreviewImage(path: String) {
        val imageBitmap = BitmapFactory.decodeFile(path)
        binding.imgStory.setImageBitmap(imageBitmap)
    }

    private fun upload() {
        with(binding) {
            if (imageFile != null && edtDescription.text.isNotEmpty()) {
                storyViewModel.addStory(AddStoryParam(edtDescription.text, imageFile!!, token, location))
            } else {
                showToast(getString(R.string.error_fill_required_field))
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    location = it
                    binding.tvLocation.text = getString(R.string.label_location, it.latitude, it.longitude)
                }.addOnFailureListener {
                    showToast(getString(R.string.error_current_location))
                }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, AddStoryActivity::class.java).apply {

            })
        }
    }
}
