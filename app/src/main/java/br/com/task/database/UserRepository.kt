package br.com.task.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.com.task.constants.DataBaseConstants
import br.com.task.model.entity.User

/**
 * Created by maher on 20/03/2018.
 */
class UserRepository private constructor(context: Context) {

    private var dataBaseHelper: DataBaseHelper = DataBaseHelper(context)

    //Singleton
    companion object {
        private var userRepository: UserRepository? = null

        fun getInstanceUserRepository(context: Context): UserRepository {
            if (userRepository == null) {
                userRepository = UserRepository(context)
            }
            return userRepository as UserRepository
        }
    }

    fun insert(user: User): Int {
        val db = dataBaseHelper.writableDatabase

        val insertValules = ContentValues()
        insertValules.put(DataBaseConstants.User.COLUMNS.NAME, user.name)
        insertValules.put(DataBaseConstants.User.COLUMNS.EMAIL, user.email)
        insertValules.put(DataBaseConstants.User.COLUMNS.PASSWORD, user.password)

        return db.insert(DataBaseConstants.User.TABLE_NAME, null, insertValules).toInt()
    }

    fun isEmailExistent(user: User): Boolean {
        var ret: Boolean = false
        var cursor: Cursor? = null

        try {
            val db = dataBaseHelper.readableDatabase
            val columns = arrayOf(DataBaseConstants.User.COLUMNS.ID) //retorno da query
            val where = "${DataBaseConstants.User.COLUMNS.EMAIL} = ?" //Similar ao WHERE
            val whereArgs = arrayOf(user.email) //o valor que estou procurando

            //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
            cursor = db.query(DataBaseConstants.User.TABLE_NAME, columns, where, whereArgs, null, null, null)

            if (cursor != null) {
                if (cursor.count > 0) {
                    ret = true
                }
            }
            cursor.close()
        } catch (e: Exception) {
            cursor?.close()
            throw e
        }
        return ret
    }

    fun login(user: User): User? {
        var userEntity: User? = null
        var cursor: Cursor? = null

        try {
            val db = dataBaseHelper.readableDatabase
            val columns = arrayOf(DataBaseConstants.User.COLUMNS.ID,
                    DataBaseConstants.User.COLUMNS.NAME,
                    DataBaseConstants.User.COLUMNS.EMAIL,
                    DataBaseConstants.User.COLUMNS.PASSWORD)
            val where = "${DataBaseConstants.User.COLUMNS.EMAIL} = ? AND ${DataBaseConstants.User.COLUMNS.PASSWORD} = ?"
            val whereArgs = arrayOf(user.email, user.password)

            cursor = db.query(DataBaseConstants.User.TABLE_NAME, columns, where, whereArgs, null, null, null)

            if (cursor != null) {
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    userEntity = User(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.User.COLUMNS.ID)),
                            cursor.getString(cursor.getColumnIndex(DataBaseConstants.User.COLUMNS.NAME)),
                            cursor.getString(cursor.getColumnIndex(DataBaseConstants.User.COLUMNS.EMAIL)))
                }
            }
            cursor.close()

        } catch (e: Exception) {
            cursor?.close()
            return userEntity
        }
        return userEntity
    }

}