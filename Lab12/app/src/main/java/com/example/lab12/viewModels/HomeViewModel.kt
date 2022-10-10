package com.example.lab12.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel: ViewModel() {
    private val _homeState: MutableStateFlow<HomeViewModel.HomeState> = MutableStateFlow(
        HomeViewModel.HomeState.Default)
    val homeState : StateFlow<HomeViewModel.HomeState> = _homeState

    sealed class HomeState(){
        object Default : HomeState()
        object Success: HomeState()
        object Failure: HomeState()
        object Empty: HomeState()
    }
    fun setDefault(){
        _homeState.value = HomeState.Default
    }
    fun setSuccess(){
        _homeState.value = HomeState.Success
    }
    fun setFailure(){
        _homeState.value = HomeState.Failure
    }
    fun setEmpty(){
        _homeState.value = HomeState.Empty
    }
}