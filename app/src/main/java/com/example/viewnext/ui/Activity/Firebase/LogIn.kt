package com.example.viewnext.ui.Activity.Firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.ImageButton
import android.widget.ImageView
import com.example.viewnext.R
import com.example.viewnext.ui.Activity.Principal_Activity


class LogIn: AppCompatActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val botonEntrar = findViewById<Button>(R.id.botonEntrar)
        val imagenLogo = findViewById<ImageView>(R.id.imageView)
        val botonRegistro = findViewById<Button>(R.id.botonRegistrar)

        imagenLogo.setOnClickListener {

            val intent = Intent(this, Principal_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        botonRegistro.setOnClickListener {

            val intent = Intent(this, SignUp::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        val textViewOlvidadoDatos = findViewById<TextView>(R.id.textViewOlvidadoDatos)
        textViewOlvidadoDatos.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        val editTextContraseña = findViewById<EditText>(R.id.editTextContraseña)
        val botonMostrarContraseña = findViewById<ImageButton>(R.id.imageViewPassword)
        botonMostrarContraseña.setOnClickListener {

            if (editTextContraseña.transformationMethod == PasswordTransformationMethod.getInstance()) {

                editTextContraseña.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {

                editTextContraseña.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        setup()

        val colorFake= ContextCompat.getColor(this, R.color.black)
        val colorFake2= ContextCompat.getColor(this, R.color.white)
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val showColor : Boolean = Firebase.remoteConfig.getBoolean("Show_Colors")
                if (showColor){
                    botonRegistro.setBackgroundColor(colorFake)
                    botonEntrar.setBackgroundColor(colorFake)
                            botonRegistro.setTextColor(colorFake2)
                            botonEntrar.setTextColor(colorFake2)
                            botonEntrar.setText("Ya tengo una cuenta")
                            botonRegistro.setText("Terminar Registro")
                }
            }
        }

    }
    private fun setup(){
        title= "Inicio"
        val botonEntrar = findViewById<Button>(R.id.botonEntrar)
        val editTextUsuario = findViewById<EditText>(R.id.editTextUsuario)
        val editTextContraseña = findViewById<EditText>(R.id.editTextContraseña)


        botonEntrar.setOnClickListener {
            if(editTextUsuario.text.isNotEmpty()&& editTextContraseña.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextUsuario.text.toString(),
                    editTextContraseña.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful){

                            val intent = Intent(this, Principal_Activity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()

                    }else{
                        showAlert()
                    }
                }
            }else if(editTextContraseña.text.isEmpty()){
                AlertPassword()
            }
            else if(editTextUsuario.text.isEmpty()){
                AlertCorreo()
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("se ha producido un error al iniciar sesion")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog= builder.create()
        dialog.show()
    }
    private fun AlertCorreo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Datos erroneos")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog= builder.create()
        dialog.show()
    }
    private fun AlertPassword(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Datos erroneos")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog= builder.create()
        dialog.show()
    }

}