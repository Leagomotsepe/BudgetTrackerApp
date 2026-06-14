package com.example.unibudget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDao {

    // USER
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    // EXPENSE
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expense>

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getExpenseCount(): Int

    @Query("SELECT DISTINCT category FROM expenses")
    suspend fun getCategories(): List<String>

    // BUDGET
    @Insert
    suspend fun insertBudget(budget: Budget)

    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalExpenses(): Double?

    @Query("SELECT * FROM budget ORDER BY id DESC LIMIT 1")
    suspend fun getLatestBudget(): Budget?
}