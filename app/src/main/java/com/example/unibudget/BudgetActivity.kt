package com.example.unibudget

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

       db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "unibudget_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val etBudget =
            findViewById<EditText>(R.id.etBudget)

        val btnSave =
            findViewById<Button>(R.id.btnSaveBudget)

        val tvBudgetLimit =
            findViewById<TextView>(R.id.tvBudgetLimit)

        val tvTotalSpent =
            findViewById<TextView>(R.id.tvTotalSpent)

        val tvRemaining =
            findViewById<TextView>(R.id.tvRemaining)

        val tvProgress =
            findViewById<TextView>(R.id.tvProgress)

        val progressBudget =
            findViewById<ProgressBar>(R.id.progressBudget)

        loadBudgetData(
            tvBudgetLimit,
            tvTotalSpent,
            tvRemaining,
            tvProgress,
            progressBudget
        )

        btnSave.setOnClickListener {

            val budgetText =
                etBudget.text.toString()

            if (budgetText.isEmpty()) {

                Toast.makeText(
                    this,
                    "Enter a budget",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val budget = Budget(
                limit = budgetText.toDouble()
            )

            CoroutineScope(Dispatchers.IO).launch {

                db.budgetDao().insertBudget(budget)

                runOnUiThread {

                    Toast.makeText(
                        this@BudgetActivity,
                        "Budget saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    etBudget.text.clear()

                    loadBudgetData(
                        tvBudgetLimit,
                        tvTotalSpent,
                        tvRemaining,
                        tvProgress,
                        progressBudget
                    )
                }
            }
        }
    }

    private fun loadBudgetData(
        tvBudgetLimit: TextView,
        tvTotalSpent: TextView,
        tvRemaining: TextView,
        tvProgress: TextView,
        progressBudget: ProgressBar
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            val totalSpent =
                db.budgetDao().getTotalExpenses() ?: 0.0

            val latestBudget =
                db.budgetDao().getLatestBudget()

            val budgetLimit =
                latestBudget?.limit ?: 0.0

            val remaining =
                budgetLimit - totalSpent

            val percentage =
                if (budgetLimit > 0)
                    ((totalSpent / budgetLimit) * 100).toInt()
                else
                    0

            runOnUiThread {

                tvBudgetLimit.text =
                    "R%.2f".format(budgetLimit)

                tvTotalSpent.text =
                    "R%.2f".format(totalSpent)

                tvRemaining.text =
                    "R%.2f".format(remaining)

                tvProgress.text =
                    "$percentage% Used"

                progressBudget.progress =
                    percentage.coerceAtMost(100)
            }
        }
    }
}