package com.example.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomdb.databinding.ActivityInsertBinding
import com.google.android.material.snackbar.Snackbar

class InsertActivity : AppCompatActivity() {
    private lateinit var dao: UserDAO

    private lateinit var binding: ActivityInsertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao= UserDB.getInstance(this).myDao

        binding.saveButton.setOnClickListener {
            val name= binding.nameEditText.text.toString()
            val email= binding.emailEditText.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty()){
                val user= User(name, email)
                val res= dao.insert(user)
                if(res==(-1).toLong()){
                    Snackbar.make(binding.insertLayout,"Insert Failed", Snackbar.LENGTH_LONG).show()
                    binding.nameEditText.text.clear()
                    binding.emailEditText.text.clear()
                }
                else{
                    Snackbar.make(binding.insertLayout,"Insert Success", Snackbar.LENGTH_LONG).show()

                    binding.nameEditText.text.clear()
                    binding.emailEditText.text.clear()
                }

            }
        }
    }
}