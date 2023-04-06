package com.example.proyectofinal.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.db.Repositorio
import com.example.proyectofinal.model.storage.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repositorio): ViewModel() {
    lateinit var prefs: Prefs

    fun inicializar(){
        viewModelScope.launch {
            val lista=readAll()
            if(!lista.isNullOrEmpty()){
                //Asignar la lista al Provider
            }
        }
    }

    fun guardarUsuario(usuario: String, c: Context){
        prefs=Prefs(c)
        prefs.setUser(usuario)
    }

    fun obtenerUsuario(c: Context): String?{
        prefs=Prefs(c)
        return prefs.getUser()
    }

    fun cerrarSesion(c: Context){
        prefs=Prefs(c)
        prefs.borrarTodo()
    }

    fun registrarUsuario(email: String, pass: String, callback:(Boolean)->Unit){
        repo.registrarUsuario(email,pass) {
            callback(it)
        }
    }

    fun entrar(email: String, pass: String, callback: (Boolean) -> Unit){
        repo.entrar(email,pass){
            callback(it)
        }
    }

    suspend fun readAll(): List<Comida>?{
        val lista=repo.readAll()
        return lista
    }


}