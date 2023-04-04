package com.example.proyectofinal.model.storage

import android.content.Context

class Prefs(c: Context){
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
