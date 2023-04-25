package com.example.proyectofinal.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.db.Repositorio
import com.example.proyectofinal.model.storage.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repositorio, private val prefs: Prefs): ViewModel() {

    fun inicializar(){
        viewModelScope.launch {
            val lista=readAll()
            if(!lista.isNullOrEmpty()){
                //Asignar la lista al Provider
            }
        }
    }

    fun guardarUsuario(usuario: String){
        prefs.setUser(usuario)
    }

    fun obtenerUsuario(): String?{
        return prefs.getUser()
    }

    fun cerrarSesion(){
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

    fun usuarioFormateado(): String{
        return prefs.getUser()!!.replace(".","-")
    }

    fun getListaIngredientes(): MutableList<Ingrediente>{
        return repo.getListaIngredientes()
    }

    fun crearIngrediente(nuevoIngr: Ingrediente, img: Uri?) {
        repo.crearIngrediente(nuevoIngr,img)
    }

    fun readIngredientes(): LiveData<MutableList<Ingrediente>>{
        var lista=MutableLiveData<MutableList<Ingrediente>>()
        repo.readIngredientes().observeForever {
            lista.value=it
        }
        return lista
    }



}