package com.miko.story.presentation.story

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityStoryBinding
import com.miko.story.presentation.membership.LoginActivity
import com.miko.story.presentation.story.adapter.StoryAdapter
import com.miko.story.utils.SettingPreferences
import com.miko.story.utils.setupToolbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StoryActivity : BaseActivity<ActivityStoryBinding>() {

    private val storyAdapter: StoryAdapter by lazy {
        StoryAdapter{

        }
    }
    private val settingPreferences: SettingPreferences by inject()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_story, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout -> {
                lifecycleScope.launch{
                    settingPreferences.clearToken()
                    LoginActivity.start(this@StoryActivity)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        with(binding){
            rvStory.apply {
                layoutManager = LinearLayoutManager(this@StoryActivity, LinearLayoutManager.VERTICAL, false)
                adapter = storyAdapter
            }
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, StoryActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
}
