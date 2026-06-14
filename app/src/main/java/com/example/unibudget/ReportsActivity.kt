package com.example.unibudget

import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

// PDF Imports
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph

class ReportsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "unibudget_db"
        ).build()

        val tvBudget = findViewById<TextView>(R.id.tvBudget)
        val tvSpent = findViewById<TextView>(R.id.tvSpent)
        val tvRemaining = findViewById<TextView>(R.id.tvRemaining)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        val listReports =
            findViewById<ListView>(R.id.listReports)

        val pieChart =
            findViewById<PieChart>(R.id.pieChart)

        val btnExportPdf =
            findViewById<Button>(R.id.btnExportPdf)

        CoroutineScope(Dispatchers.IO).launch {

            val expenses =
                db.budgetDao().getAllExpenses()

            val totalSpent =
                db.budgetDao().getTotalExpenses() ?: 0.0

            val latestBudget =
                db.budgetDao().getLatestBudget()

            val budgetLimit =
                latestBudget?.limit ?: 0.0

            val remaining =
                budgetLimit - totalSpent

            runOnUiThread {

                // Summary Cards
                tvBudget.text =
                    "R%.2f".format(budgetLimit)

                tvSpent.text =
                    "R%.2f".format(totalSpent)

                tvRemaining.text =
                    "R%.2f".format(remaining)

                // Status
                tvStatus.text =
                    when {
                        budgetLimit == 0.0 ->
                            "⚠️ No budget set"

                        totalSpent < budgetLimit * 0.8 ->
                            "✅ Excellent Budget Control"

                        totalSpent <= budgetLimit ->
                            "🎯 Approaching Budget Limit"

                        else ->
                            "🚨 Budget Exceeded"
                    }

                // Expense History
                val expenseList = expenses.map {
                    "${it.category} - R${it.amount}"
                }

                val adapter = ArrayAdapter(
                    this@ReportsActivity,
                    android.R.layout.simple_list_item_1,
                    expenseList
                )

                listReports.adapter = adapter

                // Pie Chart
                setupPieChart(
                    pieChart,
                    expenses
                )
            }

            // PDF Export Button
            runOnUiThread {

                btnExportPdf.setOnClickListener {

                    exportPdf(
                        budgetLimit,
                        totalSpent,
                        remaining,
                        expenses
                    )
                }
            }
        }
    }

    private fun setupPieChart(
        pieChart: PieChart,
        expenses: List<Expense>
    ) {

        val categoryTotals =
            mutableMapOf<String, Double>()

        for (expense in expenses) {

            categoryTotals[expense.category] =
                (categoryTotals[expense.category] ?: 0.0) +
                        expense.amount
        }

        val entries =
            ArrayList<PieEntry>()

        for ((category, total) in categoryTotals) {

            entries.add(
                PieEntry(
                    total.toFloat(),
                    category
                )
            )
        }

        val dataSet =
            PieDataSet(entries, "Categories")

        val pieData =
            PieData(dataSet)

        pieChart.data = pieData

        pieChart.description.isEnabled = false
        pieChart.centerText = "Expenses"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun exportPdf(
        budgetLimit: Double,
        totalSpent: Double,
        remaining: Double,
        expenses: List<Expense>
    ) {

        try {

            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )

            val file = File(
                downloadsFolder,
                "UniBudget_Report.pdf"
            )

            val writer = PdfWriter(file)

            val pdfDocument =
                PdfDocument(writer)

            val document =
                Document(pdfDocument)

            document.add(
                Paragraph("UniBudget Financial Report")
            )

            document.add(
                Paragraph(" ")
            )

            document.add(
                Paragraph("Budget: R$budgetLimit")
            )

            document.add(
                Paragraph("Spent: R$totalSpent")
            )

            document.add(
                Paragraph("Remaining: R$remaining")
            )

            document.add(
                Paragraph(" ")
            )

            document.add(
                Paragraph("Expense History")
            )

            for (expense in expenses) {

                document.add(
                    Paragraph(
                        "${expense.category} - R${expense.amount}"
                    )
                )
            }

            document.close()

            Toast.makeText(
                this,
                "PDF Saved to Downloads",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}