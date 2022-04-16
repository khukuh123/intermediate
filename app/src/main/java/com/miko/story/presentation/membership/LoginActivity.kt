package com.miko.story.presentation.membership

import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityLoginBinding
import com.miko.story.presentation.story.StoryActivity
import com.miko.story.utils.setupToolbar
import com.miko.story.utils.showToast

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_login), false)
    }

    override fun setupAction() {
        with(binding){
            btnLogin.setOnClickListener {
                if(edtEmail.isValid && edtPassword.isValid){
                    StoryActivity.start(this@LoginActivity)
                }else{
                    showToast(getString(R.string.error_fill_required_field))
                }
            }
            btnRegister.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
            }
        }
    }

    override fun setupProcess() {

    }

    override fun setupObserver() {

    }
}