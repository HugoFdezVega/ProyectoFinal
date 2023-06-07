package com.example.proyectofinal.viewmodel

import android.net.Uri
import android.view.Menu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.example.proyectofinal.model.Sugerencia
import com.example.proyectofinal.model.db.Repositorio
import com.example.proyectofinal.model.storage.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Main view model
 *
 * @property repo
 * @property prefs
 * @constructor Create empty Main view model
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repositorio, private val prefs: Prefs): ViewModel() {
    var ldListaMenu=MutableLiveData<MutableList<Comida>>()

    /**
     * Guardar usuario
     *
     * @param usuario
     */
    fun guardarUsuario(usuario: String){
        prefs.setUser(usuario)
    }

    /**
     * Obtener usuario
     *
     * @return
     */
    fun obtenerUsuario(): String?{
        return prefs.getUser()
    }

    /**
     * Cerrar sesion
     *
     */
    fun cerrarSesion(){
        prefs.borrarTodo()
    }

    /**
     * Registrar usuario
     *
     * @param email
     * @param pass
     * @param callback
     * @receiver
     */
    fun registrarUsuario(email: String, pass: String, callback:(Boolean)->Unit){
        repo.registrarUsuario(email,pass) {
            callback(it)
        }
    }

    /**
     * Entrar
     *
     * @param email
     * @param pass
     * @param callback
     * @receiver
     */
    fun entrar(email: String, pass: String, callback: (Boolean) -> Unit){
        repo.entrar(email,pass){
            callback(it)
        }
    }

    /**
     * Get lista ingredientes
     *
     * @return
     */
    fun getListaIngredientes(): MutableList<Ingrediente>{
        return repo.getListaIngredientes()
    }

    /**
     * Get lista comidas
     *
     * @return
     */
    fun getListaComidas(): MutableList<Comida>{
        return repo.getListaComidas()
    }

    /**
     * Get comidas veganas
     *
     * @return
     */
    fun getComidasVeganas(): MutableList<Comida>{
        return repo.getComidasVeganas()
    }

    /**
     * Get comidas gluten free
     *
     * @return
     */
    fun getComidasGlutenFree(): MutableList<Comida>{
        return repo.getComidasGlutenFree()
    }

    /**
     * Get comidas veganas gluten free
     *
     * @return
     */
    fun getComidasVeganasGlutenFree(): MutableList<Comida>{
        return repo.getComidasVeganasGlutenFree()
    }

    /**
     * Get ingr veganos
     *
     * @return
     */
    fun getIngrVeganos(): MutableList<Ingrediente>{
        return repo.getIngrVeganos()
    }

    /**
     * Get ingr gluten free
     *
     * @return
     */
    fun getIngrGlutenFree(): MutableList<Ingrediente>{
        return repo.getIngrGlutenFree()
    }

    /**
     * Get ingr veganos gluten free
     *
     * @return
     */
    fun getIngrVeganosGlutenFree(): MutableList<Ingrediente>{
        return repo.getIngrVeganosGlutenFree()
    }

    /**
     * Crear ingrediente
     *
     * @param nuevoIngr
     * @param img
     */
    fun crearIngrediente(nuevoIngr: Ingrediente, img: Uri?) {
        repo.crearIngrediente(nuevoIngr,img)
    }

    /**
     * Crear comida
     *
     * @param nuevaComida
     * @param img
     */
    fun crearComida(nuevaComida: Comida, img: Uri?){
        repo.crearComida(nuevaComida,img)
    }

    /**
     * Read ingredientes
     *
     * Recoge el LiveData del método correspondiente del repositorio y lo devuelve a quien
     * esté observando este método para que actualice lo que deba
     *
     * @return
     */
    fun readIngredientes(): LiveData<MutableList<Ingrediente>>{
        var lista=MutableLiveData<MutableList<Ingrediente>>()
        repo.readIngredientes().observeForever {
            lista.value=it
        }
        return lista
    }

    /**
     * Read comidas
     *
     * Recoge el LiveData del método correspondiente del repositorio y lo devuelve a quien
     * esté observando este método para que actualice lo que deba
     *
     * @return
     */
    fun readComidas(): LiveData<MutableList<Comida>>{

        var lista=MutableLiveData<MutableList<Comida>>()
        repo.readComidas().observeForever {
            lista.value=it
        }
        return lista
    }

    /**
     * Read menu
     *
     * Corrutina que observará los cambios en el LiveData del menú semanal y que ejecutará el método
     * para realizar una primera lectura
     *
     */
    fun readMenu(){
            repo.ldListaMenu.observeForever { menuSemanal ->
                ldListaMenu.value = menuSemanal
            }
        viewModelScope.launch {
            repo.readMenu()
        }
    }

    /**
     * Read lista compra
     *
     * @return
     */
    fun readListaCompra(): LiveData<String>{
        var listaCompra=MutableLiveData<String>()
        repo.readListaCompra().observeForever{
            listaCompra.value=it
        }
        return listaCompra
    }

    /**
     * Read sugerencias
     *
     * @return
     */
    fun readSugerencias(): LiveData<MutableList<Sugerencia>>{
        var lista=MutableLiveData<MutableList<Sugerencia>>()
        repo.readSugerencias().observeForever{
            lista.value=it
        }
        return lista
    }

    /**
     * Generar menu
     *
     * @param vegano
     * @param glutenFree
     */
    fun generarMenu(vegano: Boolean, glutenFree: Boolean){
        repo.generarMenu(vegano, glutenFree)
    }

    /**
     * Otra comida
     *
     * @param posicion
     * @param vegano
     * @param glutenFree
     * @return
     */
    fun otraComida(posicion: Int, vegano: Boolean, glutenFree: Boolean): Comida{
        return repo.otraComida(posicion, vegano, glutenFree)
    }

    /**
     * Comida parecida
     *
     * @param comida
     * @param posicion
     * @param vegano
     * @param glutenFree
     * @return
     */
    fun comidaParecida(comida: Comida, posicion: Int, vegano: Boolean, glutenFree: Boolean): Comida{
        return repo.comidaParecida(comida, posicion, vegano, glutenFree)
    }

    /**
     * Guardar menu
     *
     * @param menu
     */
    fun guardarMenu(menu: MutableList<Comida>){
        repo.guardarMenu(menu)
    }

    /**
     * Guardar lista compra
     *
     * @param listaCompra
     */
    fun guardarListaCompra(listaCompra: String){
        repo.guardarListaCompra(listaCompra)
    }

    /**
     * Guardar sugerencia
     *
     * @param sugerencia
     */
    fun guardarSugerencia(sugerencia: Sugerencia){
        repo.guardarSugerencia(sugerencia)
    }

    /**
     * Borrar comida
     *
     * @param comida
     */
    fun borrarComida(comida: Comida){
        repo.borrarComida(comida)
    }

    /**
     * Borrar ingrediente
     *
     * @param ingr
     */
    fun borrarIngrediente(ingr: Ingrediente){
        repo.borrarIngrediente(ingr)
    }

    /**
     * Borrar sugerencia
     *
     * @param sugerencia
     */
    fun borrarSugerencia(sugerencia: Sugerencia){
        repo.borrarSugerencia(sugerencia)
    }







}