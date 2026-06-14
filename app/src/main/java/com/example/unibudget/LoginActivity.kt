package com.example.unibudget

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "unibudget_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        // SharedPreferences for Remember Me
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Views
        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val cbRememberMe = findViewById<CheckBox>(R.id.cbRememberMe)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        // Load saved user (if Remember Me was checked)
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")

        username.setText(savedUsername)
        password.setText(savedPassword)

        if (!savedUsername.isNullOrEmpty()) {
            cbRememberMe.isChecked = true
        }

        // 🔐 REGISTER (simple version using same fields)
        tvRegister.setOnClickListener {

            val userText = username.text.toString()
            val passText = password.text.toString()

            if (userText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                username = userText,
                password = passText
            )

            CoroutineScope(Dispatchers.IO).launch {
                db.budgetDao().insertUser(user)

                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "User Registered", Toast.LENGTH_SHORT).show()
                    username.text.clear()
                    password.text.clear()
                }
            }
        }

        // 🔑 LOGIN
        btnLogin.setOnClickListener {

            val userText = username.text.toString()
            val passText = password.text.toString()

            if (userText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {

                val user = db.budgetDao().login(userText, passText)

                runOnUiThread {
                    if (user != null) {

                        // Save if Remember Me checked
                        if (cbRememberMe.isChecked) {
                            val editor = sharedPreferences.edit()
                            editor.putString("username", userText)
                            editor.putString("password", passText)
                            editor.apply()
                        } else {
                            sharedPreferences.edit().clear().apply()
                        }

                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("USERNAME", userText)
                        startActivity(intent)
                        username.text.clear()
                        password.text.clear()

                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}