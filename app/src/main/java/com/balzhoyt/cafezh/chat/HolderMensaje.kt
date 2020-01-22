package com.balzhoyt.cafezh.chat


import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balzhoyt.cafezh.R


/**
 * Created by user on 04/09/2017. 04
 */
class HolderMensaje constructor(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    private var nombre: TextView
    private var mensaje: TextView
    private var hora: TextView
    private var fotoMensajePerfil: de.hdodenhof.circleimageview.CircleImageView
    private var fotoMensaje: android.widget.ImageView
    fun getNombre(): TextView {
        return nombre
    }

    fun setNombre(nombre: TextView): kotlin.Unit {
        this.nombre = nombre
    }

    fun getMensaje(): TextView {
        return mensaje
    }

    fun setMensaje(mensaje: TextView): kotlin.Unit {
        this.mensaje = mensaje
    }

    fun getHora(): TextView {
        return hora
    }

    fun setHora(hora: TextView): kotlin.Unit {
        this.hora = hora
    }

    fun getFotoMensajePerfil(): de.hdodenhof.circleimageview.CircleImageView {
        return fotoMensajePerfil
    }

    fun setFotoMensajePerfil(fotoMensajePerfil: de.hdodenhof.circleimageview.CircleImageView): kotlin.Unit {
        this.fotoMensajePerfil = fotoMensajePerfil
    }

    fun getFotoMensaje(): android.widget.ImageView {
        return fotoMensaje
    }

    fun setFotoMensaje(fotoMensaje: android.widget.ImageView): kotlin.Unit {
        this.fotoMensaje = fotoMensaje
    }

    init {
        nombre = itemView.findViewById<android.view.View>(R.id.nombreMensaje) as TextView
        mensaje = itemView.findViewById<android.view.View>(R.id.mensajeMensaje) as TextView
        hora = itemView.findViewById<android.view.View>(R.id.horaMensaje) as TextView
        fotoMensajePerfil = itemView.findViewById<android.view.View>(R.id.fotoPerfilMensaje) as de.hdodenhof.circleimageview.CircleImageView
        fotoMensaje = itemView.findViewById<android.view.View>(R.id.mensajeFoto) as android.widget.ImageView
    }
}