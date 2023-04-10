package com.example.proyectofinal.model.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(@ApplicationContext c: Context){
    val storage=c.getSharedPreferences("USUARIOS",0)

    fun getUser(): String?{
        return storage.getString("USUARIO", null)
    }

    fun setUser(usuario: String){
        storage.edit().putString("USUARIO", usuario).apply()
    }

    fun borrarTodo(){
        storage.edit().clear().apply()
    }
}
