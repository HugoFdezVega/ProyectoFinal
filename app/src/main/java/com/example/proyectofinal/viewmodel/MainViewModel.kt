package com.example.proyectofinal.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.model.db.Repositorio
import com.example.proyectofinal.model.storage.Prefs

class MainViewModel: ViewModel() {
    var repo=Repositorio()
    lateinit var prefs: Prefs

    fun guardarUsuario(usuario: String, c: Context){
        prefs=Prefs(c)
        prefs.setUser(usuario)
    }

    fun obtenerUsuario(c: Context): String?{
        prefs=Prefs(c)
        return prefs.getUser()
    }

    fun registrarUsuario(email: String, pass: String, callback:(Boolean)->Unit){
        //return repo.registrarUsuario(email,pass)
        repo.registrarUsuario(email,pass) {
            callback(it)
        }
    }

}