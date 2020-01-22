package com.balzhoyt.cafezh

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class LoginActivity : AppCompatActivity() {
    var providers: List<AuthUI.IdpConfig>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        providers = Arrays.asList(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        showSignInOptions()
    }

    private fun showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers as MutableList<AuthUI.IdpConfig>)
                        //.setTheme(R.style.MyTheme)
                        .setLogo(R.drawable.logo)
                        .build(), MY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginActivity.Companion.MY_REQUEST_CODE) {
            val response: IdpResponse = IdpResponse.fromResultIntent(data)!!
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Toast.makeText(this, "Bienvenido! \n" + user!!.displayName, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Debe tener conexion a internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val MY_REQUEST_CODE = 123
    }
}