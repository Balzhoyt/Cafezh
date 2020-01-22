package com.balzhoyt.cafezh.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balzhoyt.cafezh.R
import com.balzhoyt.cafezh.chat.Entidades.MensajeEnviar
import com.balzhoyt.cafezh.chat.Entidades.MensajeRecibir
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class ChatBotActivity : AppCompatActivity() {

    private var fotoPerfil: CircleImageView? = null
    private var nombre: TextView? = null
    private var rvMensajes: RecyclerView? = null
    private var txtMensaje: EditText? = null
    private var btnEnviar: Button? = null
    private var cerrarSesion: Button? = null
    private var adapter: AdapterMensajes? = null
    private var btnEnviarFoto: ImageButton? = null
    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var fotoPerfilCadena: String? = null
    private var mAuth: FirebaseAuth? = null
    private var NOMBRE_USUARIO: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)


        fotoPerfil = findViewById<View>(R.id.fotoPerfilChat) as CircleImageView
        nombre = findViewById<View>(R.id.nombreChat) as TextView
        rvMensajes = findViewById<View>(R.id.rvMensajesChat) as RecyclerView
        txtMensaje = findViewById<View>(R.id.txtMensajeChat) as EditText
        btnEnviar = findViewById<View>(R.id.btnEnviarChat) as Button
        btnEnviarFoto = findViewById<View>(R.id.btnEnviarFotoChat) as ImageButton
        //cerrarSesion = root.findViewById<View>(R.id.cerrarSesion) as Button
        fotoPerfilCadena = ""
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.getReference("chat_Cafezh") //Sala de chat (chat_cafezh) version 2
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            NOMBRE_USUARIO=user.displayName
            nombre!!.text = NOMBRE_USUARIO
            val photoUrl = user.photoUrl
            fotoPerfilCadena=photoUrl.toString()
            //val imagen = ImagenCircular(context,fotoPerfil)0
            this?.let { it1 ->
                Glide.with(it1)
                    .load(photoUrl)
                    //.transform(TranformacionImagenCircular(applicationContext))
                    .into(fotoPerfil!!)
            }
        }

        adapter = this?.let { AdapterMensajes(it) }
        val l = LinearLayoutManager(this)

        rvMensajes!!.layoutManager = l
        rvMensajes!!.adapter = adapter
        btnEnviar!!.setOnClickListener {
            databaseReference?.push()?.setValue(MensajeEnviar(txtMensaje!!.text.toString(), NOMBRE_USUARIO, fotoPerfilCadena, "1", ServerValue.TIMESTAMP))
            txtMensaje!!.setText("")
        }
        /*cerrarSesion!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
        */

        btnEnviarFoto!!.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/jpeg"
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND)
        }
        fotoPerfil!!.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/jpeg"
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL)
        }

        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })

        databaseReference?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val m = dataSnapshot.getValue(MensajeRecibir::class.java)
                adapter!!.addMensaje(m!!)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}

        })
        verifyStoragePermissions(this)

    }


    private fun setScrollbar() {
        rvMensajes!!.scrollToPosition(adapter!!.itemCount - 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_SEND && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            storageReference = storage!!.getReference("imagenes_chat") //imagenes_chat
            val fotoReferencia = fileUri?.lastPathSegment?.let { storageReference!!.child(it) }
            if (fotoReferencia != null) {
                fotoReferencia.putFile(fileUri).addOnSuccessListener { taskSnapshot ->
                    val u = taskSnapshot.uploadSessionUri
                    val m = MensajeEnviar("$NOMBRE_USUARIO te ha enviado una foto", u.toString(), NOMBRE_USUARIO, fotoPerfilCadena, "2", ServerValue.TIMESTAMP)
                    databaseReference!!.push().setValue(m)
                }
            }
        }
    }


    companion object {
        private const val PHOTO_SEND = 1
        private const val PHOTO_PERFIL = 2
        fun verifyStoragePermissions(activity: Activity?): Boolean {
            val PERMISSIONS_STORAGE = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val REQUEST_EXTERNAL_STORAGE = 1
            val permission = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
                false
            } else {
                true
            }
        }
    }

}

