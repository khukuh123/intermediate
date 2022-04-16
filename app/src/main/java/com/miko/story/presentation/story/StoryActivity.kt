package com.miko.story.presentation.story

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityStoryBinding
import com.miko.story.presentation.story.adapter.StoryAdapter
import com.miko.story.utils.setupToolbar

class StoryActivity : BaseActivity<ActivityStoryBinding>() {

    private val storyAdapter: StoryAdapter by lazy {
        StoryAdapter{

        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, StoryActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }
    }

    override fun getViewBinding(): ActivityStoryBinding = ActivityStoryBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.app_name), false)
        setupRecyclerView()
    }

    override fun setupAction() {
        with(binding){
            fabAddStory.setOnClickListener {
                AddStoryActivity.start(this@StoryActivity)
            }
        }
    }

    override fun setupProcess() {

    }

    override fun setupObserver() {

    }

    private fun setupRecyclerView() {
        with(binding){
            rvStory.apply {
                layoutManager = LinearLayoutManager(this@StoryActivity, LinearLayoutManager.VERTICAL, false)
                adapter = storyAdapter
            }
        }
    }
}