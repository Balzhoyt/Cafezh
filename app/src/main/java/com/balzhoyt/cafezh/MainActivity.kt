package com.balzhoyt.cafezh

import ai.api.AIListener
import ai.api.android.AIConfiguration
import ai.api.android.AIService
import ai.api.model.AIError
import ai.api.model.AIResponse
import ai.api.model.Result
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.balzhoyt.cafezh.chat.ChatBotActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),AIListener {
    private var mTextToSpeech: TextToSpeech? = null
    private var mAIService: AIService? = null
    private val REQUEST_AUDIO_PERMISSION_RESULT = 100


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firebaseAuth=FirebaseAuth.getInstance()

        val txtUsuario = findViewById(R.id.txtUsuario) as TextView
        val ivFotoPerfil = findViewById(R.id.ivFotoPerfil) as ImageView
        val btnMicrofono=findViewById(R.id.btnMicrofono) as FloatingActionButton
        solicitarPermisosAudio()


        val user = firebaseAuth.currentUser
        user.let {
            txtUsuario.text=user!!.displayName.toString()
            val fotoUrl=user.photoUrl
            Glide.with(this).load(fotoUrl).into(ivFotoPerfil)
            }

        ivFotoPerfil.setOnClickListener {
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(this)

                // set message of alert dialog
                dialogBuilder.setMessage("Desea cerrar sesión?")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Si", DialogInterface.OnClickListener {
                        // dialog, id -> user!!.delete(); firebaseAuth.signOut();  finish()
                            dialog, id -> firebaseAuth?.signOut();  finish()
                    })
                    // negative button text and action
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Sesión de usuario")
                // show alert dialog
                alert.show()
        }


        cvEscanearHoja.setOnClickListener{ intentEscanearHoja() }
        cvComunidad.setOnClickListener {intentAbrirChat() }
        cvCafetalSaludable.setOnClickListener { intentAbrirSitioWeb() }
        cvContactarExperto.setOnClickListener { intentAbrirExpertos() }
        cvChatbot.setOnClickListener { intentAbrirPrecioCafe() }


        //DialowFlow
        val config = AIConfiguration(
            "340c0b33c6744a1094ed40ed935fab73",
            ai.api.AIConfiguration.SupportedLanguages.Spanish,
            AIConfiguration.RecognitionEngine.System
        )

        mAIService = AIService.getService(this,config)
        mAIService!!.setListener(this)
        mTextToSpeech = TextToSpeech(this, OnInitListener { })

        btnMicrofono.setOnClickListener {
            mAIService?.startListening()
        }

    }

    private fun intentAbrirSitioWeb() {
        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.anacafe.org/")
        startActivity(openURL)
    }


    private fun intentAbrirChat() {
        val i = Intent(this,ChatBotActivity::class.java)
        startActivity(i)
    }

    private fun intentEscanearHoja() {
        val i=Intent(this,ClassifierActivity::class.java)
        startActivity(i)
    }

    private fun intentAbrirExpertos() {
        val i = Intent(this, ExpertosActivity::class.java)
        startActivity(i)
    }

    private fun intentAbrirPrecioCafe() {
        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://www.anacafe.org/exportaciones/preciosdecafe/")
        startActivity(openURL)
    }

    // Metodos para DialowFlow
    override fun onResult(response: AIResponse?) {
        val result: Result = response!!.result

        val respuesta=result.fulfillment.speech
        mTextToSpeech!!.speak(respuesta,TextToSpeech.QUEUE_FLUSH, null, null )

        /**
        if (result.action == "input.welcome") {
            mTextToSpeech!!.speak(respuesta+","+txtUsuario.text ,TextToSpeech.QUEUE_FLUSH, null, null )
        }
*/      val intencion =result.action
        when (intencion){
            "scanner"->intentEscanearHoja()
            "chat"->intentAbrirChat()
            "informacion"->intentAbrirSitioWeb()
            "experto"->intentAbrirExpertos()
            "precioCafe"->intentAbrirPrecioCafe()
            "input.unknown"->{mTextToSpeech!!.speak(respuesta+","+txtUsuario.text ,TextToSpeech.QUEUE_FLUSH, null, null )}
            "input.welcome"->{mTextToSpeech!!.speak(respuesta+","+txtUsuario.text ,TextToSpeech.QUEUE_FLUSH, null, null )}
        }

    }

    override fun onListeningStarted() {
        Log.d("tag", "Iniciado")
    }

    override fun onAudioLevel(level: Float) {
        Log.d("tag", level.toString())
    }

    override fun onError(error: AIError?) {
        Log.d("tag", error.toString())
    }

    override fun onListeningCanceled() {
        Log.d("tag", "cancelado")
    }

    override fun onListeningFinished() {
        Log.d("tag", "finalizado")
    }


    fun solicitarPermisosAudio(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) === PackageManager.PERMISSION_GRANTED) { // put your code for Version>=Marshmallow
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(
                        this,
                        "Se requiere permisos de audio", Toast.LENGTH_SHORT
                    ).show()
                }
                requestPermissions(
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO
                    ), REQUEST_AUDIO_PERMISSION_RESULT
                )
            }
        } else { // put your code for Version < Marshmallow
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == REQUEST_AUDIO_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    applicationContext,
                    "No podrá hacer peticiones por voz", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}
