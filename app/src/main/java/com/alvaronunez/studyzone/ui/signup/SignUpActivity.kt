package com.alvaronunez.studyzone.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity


class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sign_up.setOnClickListener {
            if (isFormValid()){
                val email = userEmail.text.toString()
                val pass = etPassword.text.toString()
                signUp(email, pass)
            }
        }

        val focusListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) isFormValid()
        }

        userName.onFocusChangeListener = focusListener
        userEmail.onFocusChangeListener = focusListener
        etPassword.onFocusChangeListener = focusListener
        etConfirmPassword.onFocusChangeListener = focusListener
    }

    private fun signUp(email: String, pass: String) {
        loading.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    updateUserData()
                } else {
                    loading.visibility = View.GONE
                    if(task.exception?.message == "The email address is badly formatted.") userEmail.error = "Formato inválido"
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUserData(){
        val username = userName.text.toString()
        val lastName = lastName.text.toString()
        val user = mAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("$username $lastName")
            .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "${user?.displayName} registrado!", Toast.LENGTH_LONG).show()
                writeNewUser(user.uid, lastName, username, user.email)
            }else{
                loading.visibility = View.GONE
                Toast.makeText(this, "Error al actualizar usuario!", Toast.LENGTH_SHORT).show()
            }
        }?: run {
            loading.visibility = View.GONE
            Toast.makeText(this, "Se ha perdido el usuario!", Toast.LENGTH_LONG).show()
        }
    }

    private fun writeNewUser(userId: String, lastName: String?, name: String, email: String?) {
        val database = FirebaseFirestore.getInstance()
        val user = UserDTO(name, lastName, email)
        database.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                loading.visibility = View.GONE
                Toast.makeText(this, "DocumentSnapshot added with ID: " + documentReference.id, Toast.LENGTH_LONG).show()
                startActivity<MainActivity>()
                finish()
            }
            .addOnFailureListener { e ->
                loading.visibility = View.GONE
                Toast.makeText(this, "Error adding document", Toast.LENGTH_LONG).show()
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidName(name: String): Boolean {
        return name != ""
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 5
    }

    private fun isValidConfirmedPassword(password: String, confirmedPassword: String): Boolean {
        return password == confirmedPassword
    }

    private fun isFormValid(): Boolean {
        if (!isValidName(userName.text.toString())) {
            userName.error = "Campo vacío"
            return false
        }
        if (!isValidEmail(userEmail.text.toString())) {
            userEmail.error = "Formato inválido"
            return false
        }
        if(!isValidPassword(etPassword.text.toString())){
            etPassword.error = "Mínimo 6 caracteres"
            return false
        }
        if (!isValidConfirmedPassword(etPassword.text.toString(), etConfirmPassword.text.toString())){
            etConfirmPassword.error = "No coincide con la contraseña"
            return false
        }
        return true
    }
}
