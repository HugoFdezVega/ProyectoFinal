package com.example.proyectofinal.model.db

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectofinal.model.Comida
import com.example.proyectofinal.model.Ingrediente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repositorio @Inject constructor() {
    private val db= FirebaseDatabase.getInstance("https://randomeater-e0c93-default-rtdb.europe-west1.firebasedatabase.app/")
    private val storage= FirebaseStorage.getInstance("gs://randomeater-e0c93.appspot.com")
    private var listaIngredientes= mutableListOf<Ingrediente>()
    private var ingrVeganos= mutableListOf<Ingrediente>()
    private var ingrGlutenFree= mutableListOf<Ingrediente>()
    private var ingrVeganosGlutenFree= mutableListOf<Ingrediente>()
    private var listaComidas= mutableListOf<Comida>()
    private var comidasVeganas= mutableListOf<Comida>()
    private var comidasGlutenFree= mutableListOf<Comida>()
    private var comidasVeganasGlutenFree= mutableListOf<Comida>()
    private var menuSemanal= mutableListOf<Comida>()
    private var listaCompra= mutableListOf<String>()

    fun registrarUsuario(email: String, pass: String, callback: (Boolean)->Unit){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }

    fun entrar(email: String, pass: String, callback: (Boolean) -> Unit){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }

    fun getListaIngredientes(): MutableList<Ingrediente>{
        return listaIngredientes
    }

    fun getListaComidas(): MutableList<Comida>{
        return listaComidas
    }

    fun getComidasVeganas(): MutableList<Comida>{
        return comidasVeganas
    }

    fun getComidasGlutenFree(): MutableList<Comida>{
        return comidasGlutenFree
    }

    fun getComidasVeganasGlutenFree(): MutableList<Comida>{
        return comidasVeganasGlutenFree
    }

    fun getIngrVeganos(): MutableList<Ingrediente>{
        return ingrVeganos
    }

    fun getIngrGlutenFree(): MutableList<Ingrediente>{
        return ingrGlutenFree
    }

    fun getIngrVeganosGlutenFree(): MutableList<Ingrediente>{
        return ingrVeganosGlutenFree
    }

    fun crearIngrediente(nuevoIngr: Ingrediente, img: Uri?) {
        if(img!=null){
            guardarImagenIngr(nuevoIngr, img)
        } else {
            guardarIngrediente(nuevoIngr)
        }
    }

    private fun guardarImagenIngr(nuevoIngr: Ingrediente, img: Uri?) {
        val imagen=storage.reference.child("ingredientes/${nuevoIngr.nombre}.png")
        val upload=imagen.putFile(img!!).addOnSuccessListener {
            //Se ha guardado la imagen
            imagen.downloadUrl.addOnSuccessListener {
                //Descargamos la imagen recién guardada y la pasamos como String al ingrediente
                nuevoIngr.imagen=it.toString()
                guardarIngrediente(nuevoIngr)
            }
                .addOnFailureListener {
                    println(it.message.toString())
                }
        }
            .addOnFailureListener{
                println(it.message.toString())
            }
    }

    private fun guardarIngrediente(nuevoIngr: Ingrediente) {
        db.getReference("ingredientes").child(nuevoIngr.nombre!!).setValue(nuevoIngr).addOnSuccessListener {
            //Se ha guardado el ingrediente, así que lo filtramos
            filtrarIngr(nuevoIngr)
        }
            .addOnFailureListener {
                println(it.message.toString())
            }
    }

    fun crearComida(nuevaComida: Comida, img: Uri?){
        if(img!=null){
            guardarImagenComida(nuevaComida, img)
        } else {
            guardarComida(nuevaComida)
        }
    }

    private fun guardarComida(nuevaComida: Comida) {
        db.getReference("comidas").child(nuevaComida.nombre!!).setValue(nuevaComida).addOnSuccessListener {
            //Se ha guardado la comida, así que la filtramos
            filtrarComida(nuevaComida)
        }
            .addOnFailureListener {
                println(it.message.toString())
            }
    }

    private fun guardarImagenComida(nuevaComida: Comida, img: Uri) {
        val imagen=storage.reference.child("comidas/${nuevaComida.nombre}.png")
        val upload=imagen.putFile(img!!).addOnSuccessListener {
            //Se ha guardado la imagen
            imagen.downloadUrl.addOnSuccessListener {
                //Descargamos la imagen recién guardada y la pasamos como String a la comida
                nuevaComida.imagen=it.toString()
                guardarComida(nuevaComida)
            }
                .addOnFailureListener {
                    println(it.message.toString())
                }
        }
            .addOnFailureListener{
                println(it.message.toString())
            }
    }

    fun readIngredientes(): LiveData<MutableList<Ingrediente>>{
        // Creamos un LiveData que devolveremos para que se le pueda poner un observer y recoger así
        //los cambios que realicemos. Conectamos con la base de datos poniéndole un listener, de
        //forma que cada vez que hagamos un cambio en la base de datos, este método lo escuchará
        //y se ejecutará automáticamente, volviendo a descargar la lista y mandándola para que los
        //observadores la actualicen automáticamente. También aplica los filtros correspondientes.
        val mutableLista=MutableLiveData<MutableList<Ingrediente>>()
        db.getReference("ingredientes").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaIngredientes.clear()
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        val ingrediente=item.getValue(Ingrediente::class.java)
                        if(ingrediente!=null){
                            listaIngredientes.add(ingrediente)
                        }
                    }
                    if(ingrVeganos.isEmpty()){
                        aplicarFiltrosIngr()
                    }
                    listaIngredientes.sortBy { it.nombre!!.lowercase() }
                    mutableLista.value=listaIngredientes
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableLista
    }

    private fun aplicarFiltrosIngr() {
        // Limpiamos las listas y filtramos todos los ingredientes
        ingrVeganos.clear()
        ingrGlutenFree.clear()
        ingrVeganosGlutenFree.clear()
        for(i in listaIngredientes){
            filtrarIngr(i)
        }
    }

    private fun filtrarIngr(i: Ingrediente) {
        // Intentamos borrar el ingrediente de cada una de las posibles listas por si se trata de
        //un update. Después la añadimos a la que corresponda y por último ordenamos las listas.
        if(i.vegano!! && i.glutenFree!!){
            ingrVeganos.remove(i)
            ingrGlutenFree.remove(i)
            ingrVeganosGlutenFree.remove(i)
            ingrVeganos.add(i)
            ingrGlutenFree.add(i)
            ingrVeganosGlutenFree.add(i)
        } else {
            if(i.vegano!!){
                ingrVeganos.remove(i)
                ingrVeganos.add(i)
            }
            else if(i.glutenFree!!){
                ingrGlutenFree.remove(i)
                ingrGlutenFree.add(i)
            } else {
                ingrVeganos.remove(i)
                ingrGlutenFree.remove(i)
                ingrVeganosGlutenFree.remove(i)
            }
        }
        ingrVeganos.sortBy { it.nombre!!.lowercase() }
        ingrGlutenFree.sortBy { it.nombre!!.lowercase() }
        ingrVeganosGlutenFree.sortBy { it.nombre!!.lowercase() }
    }

    fun readComidas(): LiveData<MutableList<Comida>>{
        // Creamos un LiveData que devolveremos para que se le pueda poner un observer y recoger así
        //los cambios que realicemos. Conectamos con la base de datos poniéndole un listener, de
        //forma que cada vez que hagamos un cambio en la base de datos, este método lo escuchará
        //y se ejecutará automáticamente, volviendo a descargar la lista y mandándola para que los
        //observadores la actualicen automáticamente. También aplica los filtros correspondientes.
        val mutableLista=MutableLiveData<MutableList<Comida>>()
        db.getReference("comidas").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaComidas.clear()
                for (comidaSnapshot in snapshot.children) {
                    val nombre = comidaSnapshot.child("nombre").getValue(String::class.java)
                    val descripcion =
                        comidaSnapshot.child("descripcion").getValue(String::class.java)
                    val tags = mutableListOf<String>()
                    for (tagsSnapshot in comidaSnapshot.child("tags").children) {
                        tags.add(tagsSnapshot.getValue(String::class.java)!!)
                    }
                    val imagen = comidaSnapshot.child("imagen").getValue(String::class.java)
                    val ingredientes = mutableListOf<Ingrediente>()
                    for (ingredienteSnapshot in comidaSnapshot.child("ingredientes").children) {
                        val ingrediente = ingredienteSnapshot.getValue(Ingrediente::class.java)
                        ingredientes.add(ingrediente!!)
                    }
                    val preparacion = mutableListOf<String>()
                    for (preparacionSnapshot in comidaSnapshot.child("preparacion").children) {
                        preparacion.add(preparacionSnapshot.getValue(String::class.java)!!)
                    }
                    val raciones = comidaSnapshot.child("raciones").getValue(Int::class.java)!!
                    listaComidas.add(Comida(nombre, descripcion, tags, imagen, ingredientes, preparacion, raciones))
                }
                if(comidasVeganas.isEmpty()){
                    aplicarFiltrosComidas()
                }
                listaComidas.sortBy { it.nombre!!.lowercase() }
                mutableLista.value=listaComidas
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableLista
    }

    private fun aplicarFiltrosComidas() {
        // Limpiamos las listas y filtramos todas las comidas
        comidasVeganas.clear()
        comidasGlutenFree.clear()
        comidasVeganasGlutenFree.clear()
        for(c in listaComidas){
            filtrarComida(c)
        }
    }

    private fun filtrarComida(comida: Comida) {
        // Creamos dos banderas para cada una de las listas e iteramos la lista de ingredientes de
        //la comida. Si el ingrediente iterado no es vegano o glutenFree, se actualiza la bandera
        //correspondiente. De ser false ambas banderas, salimos de la iteración y procedemos a filtrar
        //la comida según el resultado de las banderas. Intentamos borrar la comida de cada una de las
        // posibles listas por si se trata de un update. Después la añadimos a la que corresponda y
        // por último ordenamos las listas.
        var vegana=true
        var glutenFree=true
        for(i in comida.ingredientes!!){
            if(!i.vegano!!){
                vegana=false
            }
            if(!i.glutenFree!!){
                glutenFree=false
            }
            if(!vegana&&!glutenFree){
                break
            }
        }
        if(vegana&&glutenFree){
            comidasVeganasGlutenFree.remove(comida)
            comidasVeganas.remove(comida)
            comidasGlutenFree.remove(comida)
            comidasVeganasGlutenFree.add(comida)
            comidasVeganas.add(comida)
            comidasGlutenFree.add(comida)
        } else {
            if(vegana){
                comidasVeganas.remove(comida)
                comidasVeganas.add(comida)
            }
            else if(glutenFree){
                comidasGlutenFree.remove(comida)
                comidasGlutenFree.add(comida)
            } else {
                comidasVeganasGlutenFree.remove(comida)
                comidasVeganas.remove(comida)
                comidasGlutenFree.remove(comida)
            }
        }
        comidasVeganas.sortBy { it.nombre!!.lowercase() }
        comidasGlutenFree.sortBy { it.nombre!!.lowercase() }
        comidasVeganasGlutenFree.sortBy { it.nombre!!.lowercase() }
    }


}