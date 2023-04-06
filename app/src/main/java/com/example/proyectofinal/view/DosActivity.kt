package com.example.proyectofinal.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityDosBinding
import com.example.proyectofinal.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DosActivity : AppCompatActivity(), InterfazMenuBar {
    private val fragments= arrayOf(HomeFragment(),ListasFragment())
    private val vm: MainViewModel by viewModels()

    lateinit var binding: ActivityDosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun botonPulsado(boton: Int) {
        if(boton!=2){
            cargarFragment(fragments[boton] as Fragment)
        } else {
            FirebaseAuth.getInstance().signOut()
            vm.cerrarSesion(this)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun cargarFragment(fragment: Fragment){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcGenerico,fragment)
            addToBackStack(null)
        }
    }


}