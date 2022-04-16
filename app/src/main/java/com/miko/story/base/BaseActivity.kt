package com.miko.story.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    private var mBinding: T? = null
    protected val binding
        get() = mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getViewBinding()
        setContentView(mBinding?.root)
        setupIntent()
        setupUI()
        setupAction()
        setupProcess()
        setupObserver()
    }

    abstract fun getViewBinding(): T

    abstract fun setupIntent()

    abstract fun setupUI()

    abstract fun setupAction()

    abstract fun setupProcess()

    abstract fun setupObserver()

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}