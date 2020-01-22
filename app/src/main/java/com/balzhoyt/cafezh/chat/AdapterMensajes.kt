package com.balzhoyt.cafezh.chat


import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.balzhoyt.cafezh.R
import com.balzhoyt.cafezh.chat.Entidades.MensajeRecibir


import com.bumptech.glide.Glide


/**
 * Created by user on 04/09/2017. 04
 */
class AdapterMensajes(context: Context) : RecyclerView.Adapter<HolderMensaje>() {
    private val listMensaje: MutableList<MensajeRecibir> = java.util.ArrayList<MensajeRecibir>()
    private val context: Context
    fun addMensaje(m: MensajeRecibir): kotlin.Unit {
        listMensaje.add(m)
        notifyItemInserted(listMensaje.size)
    }

    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMensaje {
        val v: android.view.View = LayoutInflater.from(context).inflate(R.layout.card_view_mensajes, parent, false)
        return HolderMensaje(v)
    }

    public override fun onBindViewHolder(holder: HolderMensaje, position: Int): kotlin.Unit {
        holder.getNombre().setText(listMensaje.get(position).nombre)
        holder.getMensaje().setText(listMensaje.get(position).mensaje)
        if ((listMensaje.get(position).type_mensaje == "2")) {
            holder.getFotoMensaje().setVisibility(android.view.View.VISIBLE)
            holder.getMensaje().setVisibility(android.view.View.VISIBLE)
            Glide.with(context).load(listMensaje.get(position).urlFoto).into(holder.getFotoMensaje())
        } else if ((listMensaje.get(position).type_mensaje == "1")) {
            holder.getFotoMensaje().setVisibility(android.view.View.GONE)
            holder.getMensaje().setVisibility(android.view.View.VISIBLE)
        }
        if (listMensaje.get(position).fotoPerfil?.isEmpty()!!) {
            holder.getFotoMensajePerfil().setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(context).load(listMensaje.get(position).fotoPerfil).into(holder.getFotoMensajePerfil())
        }
        val codigoHora: kotlin.Long = listMensaje.get(position).hora!!
        val d: java.util.Date = codigoHora.let { java.util.Date(it) }
        val sdf: java.text.SimpleDateFormat = java.text.SimpleDateFormat("hh:mm:ss a") //a pm o am
        holder.getHora().setText(sdf.format(d))
    }

    public override fun getItemCount(): Int {
        return listMensaje.size
    }

    init {
        this.context = context
    }
}