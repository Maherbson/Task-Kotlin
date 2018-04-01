package br.com.task.database

import android.content.Context
import android.database.Cursor
import br.com.task.constants.DataBaseConstants
import br.com.task.model.entity.Priority

/**
 * Created by maher on 23/03/2018.
 */
class PriorityRepository private constructor(context: Context) {

    private var mDataBaseHelper: DataBaseHelper = DataBaseHelper(context)

    companion object {
        private var INSTANCE: PriorityRepository? = null

        fun getInstance(context: Context): PriorityRepository {
            if (INSTANCE == null) {
                INSTANCE = PriorityRepository(context)
            }
            return INSTANCE as PriorityRepository
        }
    }

    fun getList(): MutableList<Priority> {
        val list = mutableListOf<Priority>()
        var cursor: Cursor? = null
        try {
            val db = mDataBaseHelper.readableDatabase

            cursor = db.rawQuery("SELECT * FROM ${DataBaseConstants.PRIORITY.TABLE_NAME}", null)

            if (cursor != null) {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        list.add(Priority(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.PRIORITY.COLUMNS.ID)),
                                cursor.getString(cursor.getColumnIndex(DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION))))
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

}