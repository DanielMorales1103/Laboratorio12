package com.example.lab12.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.lab12.R
import com.example.lab12.viewModels.SessionViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: SessionViewModel by activityViewModels()

    private lateinit var boton : Button
    private lateinit var progressBar: ProgressBar
    private lateinit var usuario :TextInputLayout
    private lateinit var contra : TextInputLayout



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boton = view.findViewById(R.id.botonIniciarSesion)
        progressBar = view.findViewById(R.id.progressBar)
        usuario = view.findViewById(R.id.ingreso_correo_profile)
        contra = view.findViewById(R.id.ingreso_contra_profile)

        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                handleLogInStatus(it)
            }
        }
    }

    private fun handleLogInStatus(status: SessionViewModel.LoginState) {
        when(status){
            is SessionViewModel.LoginState.NoLogin ->{
                boton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
            is SessionViewModel.LoginState.Login ->{
                boton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
            is SessionViewModel.LoginState.False ->{
                boton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Toast.makeText(activity, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setListeners() {
        boton.setOnClickListener {
            viewModel.viewModelScope.launch {
                boton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                delay(2000L)
                viewModel.doLogin(usuario.editText!!.text.toString(),contra.editText!!.text.toString())
            }

        }
    }

}