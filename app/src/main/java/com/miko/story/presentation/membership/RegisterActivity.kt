package com.miko.story.presentation.membership

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityRegisterBinding
import com.miko.story.presentation.story.StoryActivity
import com.miko.story.utils.observe
import com.miko.story.utils.setupToolbar
import com.miko.story.utils.showToast

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val membershipViewModel: MembershipViewModel by viewModels()

    override fun getViewBinding(): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_register), true)
    }

    override fun setupAction() {
        with(binding) {
            btnRegister.setOnClickListener {
                val name = edtName.text.isNotEmpty()
                if (name && edtEmail.isValid && edtPassword.isValid) {
                    StoryActivity.start(this@RegisterActivity)
                } else {
                    showToast(getString(R.string.error_fill_required_field))
                }
            }
        }
    }

    override fun setupProcess() {

    }

    override fun setupObserver() {
        membershipViewModel.registerResult.observe(this,
            onLoading = {
                showToast("Loading")
            },
            onSuccess = {
                showToast(getString(R.string.message_registration_success))
                LoginActivity.start(this)
            },
            onError = {
                showToast(it)
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java).apply {

            })
        }
    }
}
