package com.example.proyectofinal.model.db

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectofinal.model.Comida
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repositorio @Inject constructor() {
    private val db= FirebaseDatabase.getInstance("https://randomeater-e0c93-default-rtdb.europe-west1.firebasedatabase.app/")
    private val storage= FirebaseStorage.getInstance("gs://randomeater-e0c93.appspot.com")

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

}