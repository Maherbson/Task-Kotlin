package br.com.task.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.task.constants.DataBaseConstants

/**
 * Created by maher on 20/03/2018.
 */
class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION: Int = 1
        private val DATABASE_NAME: String = "task.db"
    }

    private val createTableUser = """
        CREATE TABLE IF NOT EXISTS ${DataBaseConstants.User.TABLE_NAME} (
        ${DataBaseConstants.User.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.User.COLUMNS.NAME} TEXT,
        ${DataBaseConstants.User.COLUMNS.EMAIL} TEXT,
        ${DataBaseConstants.User.COLUMNS.PASSWORD} TEXT);
    """.trimIndent()

    private val createTablePriority = """
        CREATE TABLE IF NOT EXISTS ${DataBaseConstants.PRIORITY.TABLE_NAME} (
        ${DataBaseConstants.PRIORITY.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION} TEXT);
    """.trimIndent()

    private val createTableTask = """
        CREATE TABLE IF NOT EXISTS ${DataBaseConstants.TASK.TABLE_NAME} (
        ${DataBaseConstants.TASK.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.TASK.COLUMNS.USERID} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.PRIORITYID} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.DESCRIPTION} TEXT,
        ${DataBaseConstants.TASK.COLUMNS.COMPLETE} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.DUEDATE} TEXT);
    """.trimIndent()

    private val insertPriority = """INSERT INTO ${DataBaseConstants.PRIORITY.TABLE_NAME}
             VALUES (1, 'Baixa'), (2, 'Média'), (3, 'Alta'), (4, 'Crítica')"""

    private var lCreate = listOf(createTableUser, createTablePriority, createTableTask, insertPriority)

    private val deleteTableUser = "DROP TABLE IF EXISTS ${DataBaseConstants.User.TABLE_NAME}"
    private val deleteTablePriority = "DROP TABLE IF EXISTS ${DataBaseConstants.PRIORITY.TABLE_NAME}"
    private val deleteTableTask = "DROP TABLE IF EXISTS ${DataBaseConstants.TASK.TABLE_NAME}"

    private val lDelete = listOf(deleteTableUser, deleteTablePriority, deleteTableTask)

    override fun onCreate(sqlLite: SQLiteDatabase) {
        for (i in 0 until lCreate.size) {
            sqlLite.execSQL(lCreate[i])
        }
    }

    override fun onUpgrade(sqlLite: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        for (i in 0 until lDelete.size) {
            sqlLite.execSQL(lDelete[i])
        }
    }
}