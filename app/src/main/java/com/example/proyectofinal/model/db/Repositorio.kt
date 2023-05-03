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

    suspend fun readAll():List<Comida>?{
        return withContext(Dispatchers.IO){
            val lista= mutableListOf<Comida>()

            //Añadimos aqui la descarga de todas las comidas de Firebase

            lista?: emptyList<Comida>()
        }
    }

    fun getListaIngredientes(): MutableList<Ingrediente>{
        return listaIngredientes
    }

    fun getListaComidas(): MutableList<Comida>{
        return listaComidas
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
            //Se ha guardado el ingrediente, así que lo añadimos a la lista y la ordenamos
            listaIngredientes.add(nuevoIngr)
            listaIngredientes.sortBy {it.nombre}
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
            //Se ha guardado la comida, así que la añadimos a la lista y la ordenamos
            //listaComidas.add(nuevaComida)
            listaComidas.sortBy {it.nombre}
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
                //Descargamos la imagen recién guardada y la pasamos como String al ingrediente
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

    fun readIngrSelect(): LiveData<MutableList<Ingrediente>>{
        val mutableLista=MutableLiveData<MutableList<Ingrediente>>()
        val lista= mutableListOf<Ingrediente>()
        db.getReference("ingredientes").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        val ingrediente=item.getValue(Ingrediente::class.java)
                        if(ingrediente!=null){
                            lista.add(ingrediente)
                        }
                    }
                    lista.sortBy { it.nombre }
                    mutableLista.value=lista
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return mutableLista
    }

    suspend fun readComidas(callback: (Boolean) -> Unit) {
        return withContext(Dispatchers.IO) {

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
                    callback(true)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }




}