package com.example.unibudget

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {

                val intent =
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                cameraLauncher.launch(intent)

            } else {

                Toast.makeText(
                    this,
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val cameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                val bitmap =
                    result.data?.extras?.get("data") as? Bitmap

                if (bitmap != null) {

                    runTextRecognition(bitmap)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        db = AppDatabase.getDatabase(this)

        val etAmount =
            findViewById<EditText>(R.id.etAmount)

        val etDescription =
            findViewById<EditText>(R.id.etDescription)

        val etCustomCategory =
            findViewById<EditText>(R.id.etCustomCategory)

        val spinner =
            findViewById<Spinner>(R.id.spCategory)

        val btnSave =
            findViewById<Button>(R.id.btnSaveExpense)

        val btnCamera =
            findViewById<Button>(R.id.btnCamera)

        val categories = arrayOf(
            "Food",
            "Transport",
            "Education",
            "Housing",
            "Utilities",
            "Healthcare",
            "Entertainment",
            "Shopping",
            "Personal Care",
            "Other"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        spinner.adapter = adapter

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (spinner.selectedItem.toString() == "Other") {

                        etCustomCategory.visibility =
                            View.VISIBLE

                    } else {

                        etCustomCategory.visibility =
                            View.GONE
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

        btnCamera.setOnClickListener {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val intent =
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                cameraLauncher.launch(intent)

            } else {

                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }

        btnSave.setOnClickListener {

            val amountText =
                etAmount.text.toString()

            val descriptionText =
                etDescription.text.toString()

            val category =
                if (spinner.selectedItem.toString() == "Other")
                    etCustomCategory.text.toString()
                else
                    spinner.selectedItem.toString()

            if (amountText.isEmpty()) {

                Toast.makeText(
                    this,
                    "Enter amount",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val expense = Expense(
                amount = amountText.toDouble(),
                category = category,
                description = descriptionText
            )

            CoroutineScope(Dispatchers.IO).launch {

                db.budgetDao().insertExpense(expense)
            }

            Toast.makeText(
                this,
                "Expense Saved",
                Toast.LENGTH_SHORT
            ).show()

            etAmount.text.clear()
            etDescription.text.clear()
            etCustomCategory.text.clear()
        }
    }

    private fun runTextRecognition(bitmap: Bitmap) {

        val image =
            InputImage.fromBitmap(bitmap, 0)

        val recognizer =
            TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS
            )

        recognizer.process(image)
            .addOnSuccessListener { visionText ->

                val extractedText =
                    visionText.text

                val regex =
                    Regex("\\d+(\\.\\d+)?")

                val matches =
                    regex.findAll(extractedText)

                val amounts =
                    matches.map {
                        it.value.toDouble()
                    }.toList()

                if (amounts.isNotEmpty()) {

                    val amount =
                        amounts.maxOrNull().toString()

                    findViewById<EditText>(
                        R.id.etAmount
                    ).setText(amount)

                    Toast.makeText(
                        this,
                        "Amount detected: $amount",
                        Toast.LENGTH_LONG
                    ).show()

                } else {

                    Toast.makeText(
                        this,
                        "No amount found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Text recognition failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}