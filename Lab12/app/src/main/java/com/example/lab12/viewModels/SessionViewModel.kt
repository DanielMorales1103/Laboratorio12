package com.example.lab12.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.contracts.contract

class SessionViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.NoLogin)
    val uiState : StateFlow<LoginState> = _uiState
    private val info = "mor21785@uvg.edu.gt"
    private lateinit var job: Job
    sealed class LoginState(){
        object NoLogin : LoginState()
        object Login: LoginState()
        object False: LoginState()
    }

    fun doLogin(correo: String, contra: String){
        viewModelScope.launch {
            if(correo.compareTo(info)==0 && contra.compareTo(info)==0){
                job = viewModelScope.launch {
                    _uiState.value =LoginState.Login
                    delay(30000L)
                    _uiState.value = LoginState.NoLogin
                }
            }else{
                _uiState.value = LoginState.False
            }
        }
    }

    fun mantenerSesion(){
        if(this::job.isInitialized && job.isActive){
            job.cancel()
        }

    }
    fun cerrarSesion(){
        job.cancel()
        _uiState.value = LoginState.NoLogin
    }



}