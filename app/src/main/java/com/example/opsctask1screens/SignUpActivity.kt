package com.example.opsctask1screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.opsctask1screens.databinding.ActivitySignUpBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("tblUsers")

        binding.btnSignUp.setOnClickListener {
            val signupUsername = binding.txtName.text.toString()
            val signupSurname = binding.txtSurname.text.toString()
            val signupEmail = binding.txtEmail.text.toString()
            val signupPassword = binding.txtPassword.text.toString()
            val signupConfirm = binding.txtConfirm.text.toString()

            if(signupUsername.isNotEmpty() && signupSurname.isNotEmpty() &&
                signupEmail.isNotEmpty() && signupPassword.isNotEmpty() && signupConfirm.isNotEmpty())
            {
                    signupUser(signupUsername, signupSurname,signupEmail,signupPassword)

            }else
            {
                Toast.makeText(this@SignUpActivity, "All fields are mandatory", Toast.LENGTH_SHORT)
                    .show()
            }

        }



    }

    private fun signupUser(username:String,surname:String,
                           email:String, password:String)
    {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if(!dataSnapshot.exists())
                {
                    val id = databaseReference.push().key
                    val userDate = UserDate(id, username, surname, email, password)
                    databaseReference.child(id!!).setValue(userDate)
                    Toast.makeText(this@SignUpActivity, "Signup Successful", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    finish()
                }
                else
                {
                    Toast.makeText(this@SignUpActivity, "User already exists", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}