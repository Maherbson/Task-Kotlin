package br.com.task.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by maher on 21/03/2018.
 */
class SharedPref(context: Context) {

    private val mSharedPref: SharedPreferences = context.getSharedPreferences("task", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) = mSharedPref.edit().putString(key, value).apply()

    fun getStoreString(key: String): String = mSharedPref.getString(key, "")

    fun deleteStoreString(key: String) = mSharedPref.edit().remove(key).apply()

}