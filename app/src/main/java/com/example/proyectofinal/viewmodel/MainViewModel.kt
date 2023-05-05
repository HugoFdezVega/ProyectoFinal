package com.example.proyectofinal.viewmodel

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


@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repositorio, private val prefs: Prefs): ViewModel() {

    fun inicializar(){
        readIngredientes()
        readComidas()
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

    fun usuarioFormateado(): String{
        return prefs.getUser()!!.replace(".","-")
    }

    fun getListaIngredientes(): MutableList<Ingrediente>{
        return repo.getListaIngredientes()
    }

    fun getListaComidas(): MutableList<Comida>{
        return repo.getListaComidas()
    }

    fun getComidasVeganas(): MutableList<Comida>{
        return repo.getComidasVeganas()
    }

    fun getComidasGlutenFree(): MutableList<Comida>{
        return repo.getComidasGlutenFree()
    }

    fun getComidasVeganasGlutenFree(): MutableList<Comida>{
        return repo.getComidasVeganasGlutenFree()
    }

    fun getIngrVeganos(): MutableList<Ingrediente>{
        return repo.getIngrVeganos()
    }

    fun getIngrGlutenFree(): MutableList<Ingrediente>{
        return repo.getIngrGlutenFree()
    }

    fun getIngrVeganosGlutenFree(): MutableList<Ingrediente>{
        return repo.getIngrVeganosGlutenFree()
    }

    fun crearIngrediente(nuevoIngr: Ingrediente, img: Uri?) {
        repo.crearIngrediente(nuevoIngr,img)
    }

    fun crearComida(nuevaComida: Comida, img: Uri?){
        repo.crearComida(nuevaComida,img)
    }

    fun readIngredientes(): LiveData<MutableList<Ingrediente>>{
        // Recoge el LiveData del método correspondiente del repositorio y lo devuelve a quien
        //esté observando este método para que actualice lo que deba
        var lista=MutableLiveData<MutableList<Ingrediente>>()
        repo.readIngredientes().observeForever {
            lista.value=it
        }
        return lista
    }

    fun readComidas(): LiveData<MutableList<Comida>>{
        // Recoge el LiveData del método correspondiente del repositorio y lo devuelve a quien
        //esté observando este método para que actualice lo que deba
        var lista=MutableLiveData<MutableList<Comida>>()
        repo.readComidas().observeForever {
            lista.value=it
        }
        return lista
    }





}