package com.miko.story.presentation.story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityStoryDetailBinding
import com.miko.story.domain.model.Story
import com.miko.story.utils.BundleKeys
import com.miko.story.utils.setImageFromUrl
import com.miko.story.utils.setupToolbar

class StoryDetailActivity : BaseActivity<ActivityStoryDetailBinding>() {

    private lateinit var story: Story

    override fun getViewBinding(): ActivityStoryDetailBinding =
        ActivityStoryDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {
        story = intent?.getParcelableExtra<Story>(BundleKeys.STORY) as Story
    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_story_detail), true)
    }

    override fun setupAction() { }

    override fun setupProcess() {
        setStoryDetail(story)
    }

    override fun setupObserver() { }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStoryDetail(story: Story) {
        with(binding){
            imgStory.setImageFromUrl(story.photoUrl)
            tvName.text = story.name
            tvDescription.text = story.description
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context, story: Story) {
            context.startActivity(Intent(context, StoryDetailActivity::class.java).apply {
                putExtra(BundleKeys.STORY, story)
            })
        }
    }
}
