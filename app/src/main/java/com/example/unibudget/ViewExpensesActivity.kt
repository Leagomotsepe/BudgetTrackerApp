package com.example.unibudget

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewExpensesActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_expenses)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "unibudget_db"
        ).build()

        val listView = findViewById<ListView>(R.id.listExpenses)

        loadExpenses(listView)
    }

    private fun loadExpenses(listView: ListView) {

        CoroutineScope(Dispatchers.IO).launch {

            val expenses = db.budgetDao().getAllExpenses()

            val formattedList = expenses.map {
                "R${it.amount} • ${it.category}\n${it.description}"
            }

            runOnUiThread {

                val adapter = object : ArrayAdapter<Expense>(
                    this@ViewExpensesActivity,
                    0,
                    expenses
                ) {
                    override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {

                        val view = layoutInflater.inflate(R.layout.item_expense, parent, false)

                        val expense = expenses[position]

                        view.findViewById<TextView>(R.id.tvAmount).text = "R${expense.amount}"
                        view.findViewById<TextView>(R.id.tvCategory).text = expense.category
                        view.findViewById<TextView>(R.id.tvDescription).text = expense.description

                        return view
                    }
                }

                listView.adapter = adapter
            }
        }
    }
}