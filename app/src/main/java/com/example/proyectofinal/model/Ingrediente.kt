package com.example.proyectofinal.model

class Ingrediente(
    var nombre: String?=null,
    var cantidad: Double?=0.0,
    var unidad: String?=null,
    var imagen: String?= null
):java.io.Serializable {

    //Métodos equals y hashcode sobreescritos para que detecten como iguales dos
    //ingredientes que tengan el mismo nombre
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Ingrediente
        if (nombre != other.nombre) return false
        return true
    }
    override fun hashCode(): Int {
        return nombre.hashCode()
    }

    //Constructor copia para evitar problemas de aliasing
    constructor(ingrediente: Ingrediente) : this(ingrediente.nombre, ingrediente.cantidad, ingrediente.unidad, ingrediente.imagen)


}