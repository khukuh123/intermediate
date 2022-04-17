package com.miko.story.presentation.story

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityAddStoryBinding
import com.miko.story.utils.setupToolbar

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {

    override fun getViewBinding(): ActivityAddStoryBinding =
        ActivityAddStoryBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_add_story), true)
    }

    override fun setupAction() {
        with(binding) {
            btnCamera.setOnClickListener {

            }
            btnGallery.setOnClickListener {

            }
            btnUpload.setOnClickListener {

            }
        }
    }

    override fun setupProcess() {

    }

    override fun setupObserver() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, AddStoryActivity::class.java).apply {

            })
        }
    }
}
