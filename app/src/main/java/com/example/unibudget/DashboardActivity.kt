package com.example.unibudget

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db=Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "unibudget_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val greeting = findViewById<TextView>(R.id.tvGreeting)
        val welcome = findViewById<TextView>(R.id.tvWelcome)

        val username = intent.getStringExtra("USERNAME") ?: "User"

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greeting.text = when {
            hour < 12 -> "Good Morning, $username 👋"
            hour < 18 -> "Good Afternoon, $username 👋"
            else -> "Good Evening, $username 👋"
        }

        welcome.text = when {
            hour < 12 -> "Let's start the day financially strong."
            hour < 18 -> "Keep tracking your spending today."
            else -> "Here's your spending summary for today."
        }

        val tvBudgetAmount =
            findViewById<TextView>(R.id.tvBudgetAmount)

        val tvSpentAmount =
            findViewById<TextView>(R.id.tvSpentAmount)

        val tvRemainingAmount =
            findViewById<TextView>(R.id.tvRemainingAmount)

        val progressBudget =
            findViewById<ProgressBar>(R.id.progressBudget)

        val tvBudgetPercentage =
            findViewById<TextView>(R.id.tvBudgetPercentage)

        val tvAchievement =
            findViewById<TextView>(R.id.tvAchievement)

        loadDashboardData(
            tvBudgetAmount,
            tvSpentAmount,
            tvRemainingAmount,
            progressBudget,
            tvBudgetPercentage,
            tvAchievement
        )

        findViewById<LinearLayout>(R.id.cardAddExpense)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        AddExpenseActivity::class.java
                    )
                )
            }

        findViewById<LinearLayout>(R.id.cardViewExpenses)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        ViewExpensesActivity::class.java
                    )
                )
            }

        findViewById<LinearLayout>(R.id.cardBudget)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        BudgetActivity::class.java
                    )
                )
            }

        findViewById<LinearLayout>(R.id.cardReports)
            .setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        ReportsActivity::class.java
                    )
                )
            }
    }

    private fun loadDashboardData(
        tvBudgetAmount: TextView,
        tvSpentAmount: TextView,
        tvRemainingAmount: TextView,
        progressBudget: ProgressBar,
        tvBudgetPercentage: TextView,
        tvAchievement: TextView
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            val totalSpent =
                db.budgetDao().getTotalExpenses() ?: 0.0

            val latestBudget =
                db.budgetDao().getLatestBudget()

            val expenseCount =
                db.budgetDao().getExpenseCount()

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

                tvBudgetAmount.text =
                    "R%.2f".format(budgetLimit)

                tvSpentAmount.text =
                    "R%.2f".format(totalSpent)

                tvRemainingAmount.text =
                    "R%.2f".format(remaining)

                progressBudget.progress =
                    percentage.coerceAtMost(100)

                tvBudgetPercentage.text =
                    "$percentage% Used"

                tvAchievement.text =
                    when {

                        expenseCount == 0 ->
                            "🏅 Add your first expense"

                        expenseCount == 1 ->
                            "🥉 First Expense Unlocked"

                        expenseCount in 2..4 ->
                            "🎯 Keep tracking expenses"

                        expenseCount in 5..9 ->
                            "⭐ Expense Tracker Unlocked"

                        expenseCount in 10..19 ->
                            "🥈 Budget Champion Unlocked"

                        else ->
                            "🥇 Finance Master Unlocked"
                    }
            }
        }
    }
}