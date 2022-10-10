package com.example.lab12.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.lab12.R
import com.example.lab12.viewModels.HomeViewModel
import com.example.lab12.viewModels.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModelSession: SessionViewModel by activityViewModels()
    private val viewModelHome: HomeViewModel by viewModels()
    private lateinit var boton_MantenerSesion : Button
    private lateinit var boton_CerrarSesion : Button
    private lateinit var boton_Default : Button
    private lateinit var boton_Success : Button
    private lateinit var boton_Failure : Button
    private lateinit var boton_Empty : Button
    private lateinit var texto : TextView
    private lateinit var icono : ImageView
    private lateinit var progresbar : ProgressBar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boton_MantenerSesion = view.findViewById(R.id.boton_mantenerSesion)
        boton_CerrarSesion = view.findViewById(R.id.boton_cerrarSesion)
        boton_Default = view.findViewById(R.id.boton_default)
        boton_Success = view.findViewById(R.id.boton_success)
        boton_Failure = view.findViewById(R.id.boton_failure)
        boton_Empty = view.findViewById(R.id.boton_Empty)
        texto = view.findViewById(R.id.textIcon)
        icono = view.findViewById(R.id.icon)
        progresbar = view.findViewById(R.id.progressBarHome)
        boton_Default.isEnabled=false
        setListeners()
        setObservables()
    }

    private fun setListeners() {
        boton_MantenerSesion.setOnClickListener {
            viewModelSession.mantenerSesion()
        }
        boton_CerrarSesion.setOnClickListener {
            viewModelSession.cerrarSesion()
        }
        boton_Default.setOnClickListener {
            viewModelHome.viewModelScope.launch {
                progresbar.visibility = View.VISIBLE
                boton_Empty.isEnabled = false
                boton_Failure.isEnabled = false
                boton_Success.isEnabled = false
                icono.visibility = View.GONE
                texto.visibility = View.GONE
                delay(2000L)
                viewModelHome.setDefault()

            }

        }
        boton_Success.setOnClickListener {
            viewModelHome.viewModelScope.launch {
                progresbar.visibility = View.VISIBLE
                boton_Empty.isEnabled = false
                boton_Failure.isEnabled = false
                boton_Default.isEnabled = false
                icono.visibility = View.GONE
                texto.visibility = View.GONE
                delay(2000L)
                viewModelHome.setSuccess()
            }

        }
        boton_Failure.setOnClickListener {
            viewModelHome.viewModelScope.launch {
                progresbar.visibility = View.VISIBLE
                boton_Empty.isEnabled = false
                boton_Default.isEnabled = false
                boton_Success.isEnabled = false
                icono.visibility = View.GONE
                texto.visibility = View.GONE
                delay(2000L)
                viewModelHome.setFailure()
            }

        }
        boton_Empty.setOnClickListener {
            viewModelHome.viewModelScope.launch {
                progresbar.visibility = View.VISIBLE
                boton_Default.isEnabled = false
                boton_Failure.isEnabled = false
                boton_Success.isEnabled = false
                icono.visibility = View.GONE
                texto.visibility = View.GONE
                delay(2000L)
                viewModelHome.setEmpty()
            }

        }
    }


    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            viewModelSession.uiState.collectLatest {
                handleLogInStatus(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModelHome.homeState.collectLatest {
                handleHomeStatus(it)
            }
        }
    }

    private fun handleHomeStatus(status: HomeViewModel.HomeState) {
        when(status){
            HomeViewModel.HomeState.Default -> {
                progresbar.visibility = View.GONE
                icono.visibility = View.VISIBLE
                texto.visibility = View.VISIBLE
                boton_Empty.isEnabled = true
                boton_Failure.isEnabled = true
                boton_Success.isEnabled = true
                texto.text = "Selecciona una opción"
                icono.setImageResource(R.drawable.ic_default)
            }
            HomeViewModel.HomeState.Empty -> {
                progresbar.visibility = View.GONE
                icono.visibility = View.VISIBLE
                texto.visibility = View.VISIBLE
                boton_Default.isEnabled = true
                boton_Failure.isEnabled = true
                boton_Success.isEnabled = true
                texto.text = "Sin resultados"
                icono.setImageResource(R.drawable.ic_empty)
            }
            HomeViewModel.HomeState.Failure -> {
                progresbar.visibility = View.GONE
                icono.visibility = View.VISIBLE
                texto.visibility = View.VISIBLE
                boton_Empty.isEnabled = true
                boton_Default.isEnabled = true
                boton_Success.isEnabled = true
                texto.text = "¡Operación fallida!"
                icono.setImageResource(R.drawable.ic_failure)
            }
            HomeViewModel.HomeState.Success -> {
                progresbar.visibility = View.GONE
                icono.visibility = View.VISIBLE
                texto.visibility = View.VISIBLE
                boton_Empty.isEnabled = true
                boton_Failure.isEnabled = true
                boton_Default.isEnabled = true
                texto.text = "¡Operación exitosa!"
                icono.setImageResource(R.drawable.ic_success)
            }
        }
    }

    private fun handleLogInStatus(status: SessionViewModel.LoginState) {
        when(status){
            is SessionViewModel.LoginState.NoLogin ->{
                requireView().findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }else->{

            }
        }
    }
}