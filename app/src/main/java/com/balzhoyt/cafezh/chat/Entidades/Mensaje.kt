package com.balzhoyt.cafezh.chat.Entidades
/**
 * Created by user on 04/09/2017. 04
 */
open class Mensaje {
    var mensaje: String? = null
    var urlFoto: String? = null
    var nombre: String? = null
    var fotoPerfil: String? = null
    var type_mensaje: String? = null

    constructor() {}
    constructor(mensaje: String?, nombre: String?, fotoPerfil: String?, type_mensaje: String?) {
        this.mensaje = mensaje
        this.nombre = nombre
        this.fotoPerfil = fotoPerfil
        this.type_mensaje = type_mensaje
    }

    constructor(mensaje: String?, urlFoto: String?, nombre: String?, fotoPerfil: String?, type_mensaje: String?) {
        this.mensaje = mensaje
        this.urlFoto = urlFoto
        this.nombre = nombre
        this.fotoPerfil = fotoPerfil
        this.type_mensaje = type_mensaje
    }

}