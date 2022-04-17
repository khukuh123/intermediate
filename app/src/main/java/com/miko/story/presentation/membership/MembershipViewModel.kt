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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MembershipViewModel(private val storyUseCase: StoryUseCase, private val settingPreferences: SettingPreferences): ViewModel() {
    private val _registerResult: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val _loginResult: MutableLiveData<Resource<User>> = MutableLiveData()
    private val _logoutResult: MutableLiveData<Resource<String>> = MutableLiveData()

    val registerResult: LiveData<Resource<Boolean>> get() = _registerResult
    val loginResult: LiveData<Resource<User>> get() = _loginResult
    val logoutResult: LiveData<Resource<String>> get() = _logoutResult

    init {
        _registerResult.value = Resource.Loading()
        _loginResult.value = Resource.Loading()
        _logoutResult.value = Resource.Loading()
    }

    fun register(registerParam: RegisterParam){
        collectResult(_registerResult){
            storyUseCase.register(registerParam)
        }
    }

    fun login(loginParam: LoginParam){
        collectResult(_loginResult){
            storyUseCase.login(loginParam)
        }
    }

    fun logout(){
        viewModelScope.launch {
            settingPreferences.clearToken()
        }
    }

    private fun <T> collectResult(liveData: MutableLiveData<T>, block: suspend () -> Flow<T>){
        viewModelScope.launch {
            val result = block.invoke()
            result.collect{
                liveData.postValue(it)
            }
        }
    }
}