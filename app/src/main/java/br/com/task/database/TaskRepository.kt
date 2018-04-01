package br.com.task.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.com.task.constants.DataBaseConstants
import br.com.task.model.entity.Task
import br.com.task.model.entity.User

/**
 * Created by maher on 24/03/2018.
 */
class TaskRepository private constructor(context: Context) {

    private val mDataBaseHelper: DataBaseHelper = DataBaseHelper(context)

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }

            return INSTANCE as TaskRepository
        }
    }

    fun getListTaskByUserId(user: User, mTaskFilter: Int): MutableList<Task> {
        var list = mutableListOf<Task>()
        var cursor: Cursor? = null
        try {

            val db = mDataBaseHelper.readableDatabase

            var strBuilder = StringBuilder()
            strBuilder.append("SELECT * FROM ${DataBaseConstants.TASK.TABLE_NAME}")
            strBuilder.append(" ")
            strBuilder.append("WHERE ${DataBaseConstants.TASK.COLUMNS.USERID} = ${user.id} AND ${DataBaseConstants.TASK.COLUMNS.COMPLETE} = $mTaskFilter")

            cursor = db.rawQuery(strBuilder.toString(), null)
            if (cursor != null) {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        list.add(Task(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID)),
                                cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.USERID)),
                                cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID)),
                                cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION)),
                                cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE)),
                                (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)))
                    }
                }
            }

            cursor.close()
        } catch (e: Exception) {
            cursor?.close()
            return list
        }

        return list
    }

    fun getTaskById(tas: Task): Task? {
        var task: Task? = null
        var cursor: Cursor? = null

        try {
            val db = mDataBaseHelper.readableDatabase
            val columns = arrayOf(DataBaseConstants.TASK.COLUMNS.ID,
                    DataBaseConstants.TASK.COLUMNS.USERID,
                    DataBaseConstants.TASK.COLUMNS.PRIORITYID,
                    DataBaseConstants.TASK.COLUMNS.DESCRIPTION,
                    DataBaseConstants.TASK.COLUMNS.DUEDATE,
                    DataBaseConstants.TASK.COLUMNS.COMPLETE)

            val where = "${DataBaseConstants.TASK.COLUMNS.ID} = ?"
            val whereArgs = arrayOf(tas.id.toString())

            cursor = db.query(DataBaseConstants.TASK.TABLE_NAME, columns, where, whereArgs, null, null, null)

            if (cursor != null) {
                if (cursor.count > 0) {
                    cursor.moveToFirst()

                    task = Task(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID)),
                            cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.USERID)),
                            cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID)),
                            cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE)),
                            (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE))) == 1)
                }
            }
            cursor?.close()

        } catch (e: Exception) {
            cursor?.close()
            return task
        }

        return task
    }

    fun insertTask(task: Task) {
        try {
            val db = mDataBaseHelper.writableDatabase

            val insertValues = ContentValues()
            insertValues.put(DataBaseConstants.TASK.COLUMNS.USERID, task.userId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, task.priority)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, if (task.complete) 1 else 0)

            db.insert(DataBaseConstants.TASK.TABLE_NAME, null, insertValues)
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateTaskById(task: Task) {
        try {
            val db = mDataBaseHelper.writableDatabase

            val updateValues = ContentValues()
            updateValues.put(DataBaseConstants.TASK.COLUMNS.USERID, task.userId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, task.priority)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, if (task.complete) 1 else 0)

            val where = "${DataBaseConstants.TASK.COLUMNS.ID} = ?"
            val whereArgs = arrayOf(task.id.toString())

            db.update(DataBaseConstants.TASK.TABLE_NAME, updateValues, where, whereArgs)
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteTaskById(task: Task) {
        try {
            val db = mDataBaseHelper.writableDatabase
            val where = "${DataBaseConstants.TASK.COLUMNS.ID} = ?"
            val whereArgs = arrayOf(task.id.toString())

            db.delete(DataBaseConstants.TASK.TABLE_NAME, where, whereArgs)
        } catch (e: Exception) {
            throw e
        }

    }
}