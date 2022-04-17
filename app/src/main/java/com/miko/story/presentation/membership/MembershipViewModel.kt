package com.miko.story.presentation.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miko.story.domain.StoryUseCase
import com.miko.story.domain.model.LoginParam
import com.miko.story.domain.model.RegisterParam
import com.miko.story.domain.model.User
import com.miko.story.domain.util.Resource
import com.miko.story.utils.SettingPreferences
import com.miko.story.utils.collectResult
import kotlinx.coroutines.launch

class MembershipViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _registerResult: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val _loginResult: MutableLiveData<Resource<User>> = MutableLiveData()

    val registerResult: LiveData<Resource<Boolean>> get() = _registerResult
    val loginResult: LiveData<Resource<User>> get() = _loginResult

    fun register(registerParam: RegisterParam) {
        _registerResult.value = Resource.Loading()
        viewModelScope.collectResult(_registerResult) {
            storyUseCase.register(registerParam)
        }
    }

    fun login(loginParam: LoginParam) {
        _loginResult.value = Resource.Loading()
        viewModelScope.collectResult(_loginResult) {
            storyUseCase.login(loginParam)
        }
    }
}