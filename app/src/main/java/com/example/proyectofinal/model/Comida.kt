package com.example.proyectofinal.model

data class Comida(
    var nombre: String?=null,
    var descripcion: String?=null,
    var tags: MutableList<String>,
    var imagen: String?=null,
    var ingredientes: MutableList<Ingrediente>,
    var preparacion: MutableList<String>
    ):java.io.Serializable{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Comida
        if (nombre != other.nombre) return false
        return true
    }
    override fun hashCode(): Int {
        return nombre.hashCode()
    }
}
