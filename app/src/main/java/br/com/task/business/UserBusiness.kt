package br.com.task.business

import android.content.Context
import br.com.task.R
import br.com.task.constants.SharedPrefConstants
import br.com.task.database.UserRepository
import br.com.task.model.entity.User
import br.com.task.util.SharedPref
import br.com.task.util.ValidationException

/**
 * Created by maher on 20/03/2018.
 */
class UserBusiness(var context: Context) {

    private val mUserRepository: UserRepository = UserRepository.getInstanceUserRepository(context)
    private val mSharedPref: SharedPref = SharedPref(context)

    fun insert(user: User): Boolean {
        try {
            if (user.name == "" || user.email == "" || user.password == "") {
                throw ValidationException(context.getString(R.string.campos_vazios))
            }
            if (mUserRepository.isEmailExistent(user)) {
                throw ValidationException(context.getString(R.string.email_cadastrado))
            }

            val idUser = mUserRepository.insert(user)
            if (idUser > -1) {
                user.id = idUser
                saveDateUser(user)
                return true
            }

        } catch (e: Exception) {
            throw e
        }
        return false
    }

    fun login(user: User): Boolean {
        val userEntity: User? = mUserRepository.login(user)

        return if (userEntity != null) {
            saveDateUser(userEntity)
            true
        } else {
            false
        }
    }

    fun saveDateUser(user: User) {
        mSharedPref.saveString(SharedPrefConstants.KEY.USER_ID, user.id.toString())
        mSharedPref.saveString(SharedPrefConstants.KEY.USER_NAME, user.name)
        mSharedPref.saveString(SharedPrefConstants.KEY.USER_EMAIL, user.email)
    }

}