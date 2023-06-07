package com.example.proyectofinal.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivitySugerenciasUserBinding
import com.example.proyectofinal.model.Sugerencia
import com.example.proyectofinal.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Sugerencias user activity
 *
 * @constructor Create empty Sugerencias user activity
 */
@AndroidEntryPoint
class SugerenciasUserActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()
    private val maxCaracteres=500
    private val filtroCaracteres=InputFilter.LengthFilter(maxCaracteres)
    private var asunto=""
    private var contenido=""

    lateinit var binding: ActivitySugerenciasUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySugerenciasUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializar()
        setListeners()
    }

    /**
     * Inicializar
     *
     */
    private fun inicializar() {
        binding.tvEmailSugsUser.text=vm.obtenerUsuario()
    }

    /**
     * Set listeners
     *
     * Pone los listeners, entre ellos aquel que impide que se escriban mas de 500 caracteres y que
     * cambia el color del contador en funcion de esta cantidad
     *
     */
    private fun setListeners() {
        binding.etContenidoSugsUser.filters= arrayOf(filtroCaracteres)
        binding.etContenidoSugsUser.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val caracteresActuales= s?.length ?:0
                binding.tvCharsSugsUser.text="${caracteresActuales}/500"
                if(caracteresActuales==500){
                    binding.tvCharsSugsUser.setTextColor(Color.RED)
                }
                else if(caracteresActuales>399){
                    binding.tvCharsSugsUser.setTextColor(Color.rgb(255,160,64))
                }
                else{
                    binding.tvCharsSugsUser.setTextColor(Color.BLACK)
                }
            }
        })
        binding.btGuardarSugsUser.setOnClickListener {
            if(!existeError()){
                val sugerencia=Sugerencia(vm.obtenerUsuario(),asunto,contenido)
                vm.guardarSugerencia(sugerencia)
                Toast.makeText(this,"Sugerencia enviada con éxito. La atenderemos cuanto antes.",Toast.LENGTH_LONG).show()
                finish()
            }
        }
        binding.btVolverSugsUser.setOnClickListener {
            finish()
        }
    }

    /**
     * Existe error
     *
     * @return
     */
    private fun existeError(): Boolean {
        asunto=binding.etAsuntoSugsUser.text.toString().trim()
        contenido=binding.etContenidoSugsUser.text.toString().trim()
        if(asunto.isBlank()){
            binding.etAsuntoSugsUser.setError("El asunto no puede estar vacío")
            binding.etAsuntoSugsUser.requestFocus()
            return true
        }
        else if(contenido.isBlank()){
            binding.etContenidoSugsUser.setError("El contenido no puede estar vacío")
            binding.etContenidoSugsUser.requestFocus()
            return true
        } else {
            return false
        }
    }

}