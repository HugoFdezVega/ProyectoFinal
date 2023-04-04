package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.example.proyectofinal.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    var email=""
    var pass=""
    var resultado=false

    lateinit var vm: MainViewModel
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm=MainViewModel()
        setListeners()
        comprobarCorreo()
    }

    private fun comprobarCorreo() {
        if(vm.obtenerUsuario(this)!=null){
            startActivity(Intent(this, DosActivity::class.java))
        }
    }

    private fun setListeners() {
        binding.btRegistro.setOnClickListener {
            if(!existeError()){
                resultado=vm.registrarUsuario(email,pass)
                if(resultado){
                    vm.guardarUsuario(email, this)
                    startActivity(Intent(this, DosActivity::class.java))
                } else{
                    Toast.makeText(this, "Error en el registro", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun existeError(): Boolean {
        email=binding.etEmail.text.toString().trim()
        pass=binding.etPass.text.toString().trim()
        if(email.isBlank()){
            binding.etEmail.setError("El email no puede estar vacio")
            return true
        }
        else if(pass.isBlank()){
            binding.etPass.setError("La contraseña no puede estar vacia")
            return true
        }
        else if(pass.length<8){
            binding.etPass.setError("La contraseña debe tener al menos 8 caracteres")
            return true
        }
        return false
    }






}