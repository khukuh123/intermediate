package com.miko.story.presentation.story

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.base.StoryLoadingStateAdapter
import com.miko.story.databinding.ActivityStoryBinding
import com.miko.story.presentation.maps.StoryMapActivity
import com.miko.story.presentation.membership.LoginActivity
import com.miko.story.presentation.story.adapter.StoryAdapter
import com.miko.story.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryActivity : BaseActivity<ActivityStoryBinding>() {

    private val storyAdapter: StoryAdapter by lazy {
        StoryAdapter { data, imageView, textview ->
            val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(imageView, "story"),
                Pair(textview, "name")
            )
            StoryDetailActivity.start(this, data, optionCompat)
        }
    }
    private val settingPreferences: SettingPreferences by inject()
    private val storyViewModel: StoryViewModel by viewModel()
    private var token: String = ""

    override fun getViewBinding(): ActivityStoryBinding = ActivityStoryBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.app_name), false)
        setupRecyclerView()
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
    }

    override fun setupAction() {
        with(binding) {
            fabAddStory.setOnClickListener {
                AddStoryActivity.start(this@StoryActivity)
            }
        }
    }

    override fun setupProcess() {
        lifecycleScope.launch {
            settingPreferences.getToken().collect {
                token = it
                storyViewModel.getAllStories(token)
            }
        }
    }

    override fun setupObserver() {
        storyViewModel.storiesResult.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
        storyAdapter.addLoadStateListener {
            when (val loadState = it.source.refresh) {
                is LoadState.Loading -> {
                    if (storyAdapter.itemCount == 0) showLoading()
                }
                is LoadState.NotLoading -> {
                    dismissLoading()
                    binding.msvStories.showContent()
                }
                is LoadState.Error -> {
                    dismissLoading()
                    if (storyAdapter.itemCount == 0) {
                        showErrorDialog(loadState.error.message.orEmpty()) { storyAdapter.retry() }
                        binding.msvStories.showEmptyList(getString(R.string.empty_story), getString(R.string.empty_story_message))
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_story, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                lifecycleScope.launch {
                    settingPreferences.clearToken()
                    LoginActivity.start(this@StoryActivity)
                }
            }
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu_maps -> {
                StoryMapActivity.start(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvStory.apply {
                layoutManager = LinearLayoutManager(this@StoryActivity, LinearLayoutManager.VERTICAL, false)
                adapter = storyAdapter.withLoadStateFooter(StoryLoadingStateAdapter {
                    storyAdapter.retry()
                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, StoryActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
}
