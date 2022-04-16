package com.miko.story.presentation.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityStoryDetailBinding
import com.miko.story.utils.setImageFromUrl
import com.miko.story.utils.setupToolbar

class StoryDetailActivity : BaseActivity<ActivityStoryDetailBinding>() {
    override fun getViewBinding(): ActivityStoryDetailBinding =
        ActivityStoryDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_story_detail), true)
        setStoryDetail()
    }

    override fun setupAction() {

    }

    override fun setupProcess() {

    }

    override fun setupObserver() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStoryDetail() {
        with(binding){
            imgStory.setImageFromUrl("")
            tvName.text = "John Doe"
            tvDescription.text = "Lorem Ipsum"
        }
    }
}
