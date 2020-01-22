package com.balzhoyt.cafezh.chat.Entidades

/**
 * Created by user on 05/09/2017. 05
 */
class MensajeRecibir : Mensaje {
    var hora: Long? = null

    constructor() {}
    constructor(hora: Long?) {
        this.hora = hora
    }

    constructor(mensaje: String?, urlFoto: String?, nombre: String?, fotoPerfil: String?, type_mensaje: String?, hora: Long?) : super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje) {
        this.hora = hora
    }

}

