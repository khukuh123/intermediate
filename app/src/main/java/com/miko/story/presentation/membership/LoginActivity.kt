package com.miko.story.presentation.membership

import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityLoginBinding
import com.miko.story.domain.model.LoginParam
import com.miko.story.presentation.story.StoryActivity
import com.miko.story.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val membershipViewModel: MembershipViewModel by viewModel()
    private val settingPreferences: SettingPreferences by inject()

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_login), false)
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
    }

    override fun setupAction() {
        with(binding) {
            btnLogin.setOnClickListener {
                login()
            }
            btnRegister.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
            }
        }
    }

    override fun setupProcess() {
    }

    override fun setupObserver() {
        membershipViewModel.loginResult.observe(this,
            onLoading = {
                showLoading()
            },
            onSuccess = {
                dismissLoading()
                lifecycleScope.launch { settingPreferences.setToken(it.token) }
                StoryActivity.start(this)
            },
            onError = {
                dismissLoading()
                showErrorDialog(it) {
                    login()
                }
            }
        )
        lifecycleScope.launch {
            settingPreferences.getToken().collect {
                dismissLoading()
                StoryActivity.start(this@LoginActivity)
            }
        }
    }

    private fun login() {
        with(binding) {
            if (edtEmail.isValid && edtPassword.isValid) {
                membershipViewModel.login(LoginParam(edtEmail.text, edtPassword.text))
            } else {
                showToast(getString(R.string.error_fill_required_field))
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
}