package com.miko.story.presentation.membership

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.miko.story.R
import com.miko.story.base.BaseActivity
import com.miko.story.databinding.ActivityRegisterBinding
import com.miko.story.domain.model.RegisterParam
import com.miko.story.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val membershipViewModel: MembershipViewModel by viewModel()

    override fun getViewBinding(): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(getString(R.string.title_register), true)
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
    }

    override fun setupAction() {
        with(binding) {
            btnRegister.setOnClickListener {
                register()
            }
        }
    }

    override fun setupProcess() {

    }

    override fun setupObserver() {
        membershipViewModel.registerResult.observe(this,
            onLoading = {
                showLoading()
            },
            onSuccess = {
                dismissLoading()
                showToast(getString(R.string.message_registration_success))
                LoginActivity.start(this)
            },
            onError = {
                dismissLoading()
                showErrorDialog(it) {
                    register()
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

    private fun register() {
        with(binding) {
            val name = edtName.text.isNotEmpty()
            if (name && edtEmail.isValid && edtPassword.isValid) {
                membershipViewModel.register(
                    RegisterParam(
                        edtName.text,
                        edtEmail.text,
                        edtPassword.text
                    )
                )
            } else {
                showToast(getString(R.string.error_fill_required_field))
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java).apply {

            })
        }
    }
}
