package br.com.task.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.task.R
import br.com.task.business.UserBusiness
import br.com.task.constants.SharedPrefConstants
import br.com.task.model.entity.User
import br.com.task.util.SharedPref
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness
    private lateinit var mSharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mUserBusiness = UserBusiness(this)
        mSharedPref = SharedPref(this)

        verifyLoggedUser()
        setListener()
    }

    private fun verifyLoggedUser() {
        val userId = mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_ID)
        val userName  = mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_NAME)

        if (userId != "" && userName != "") {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setListener() {
        btLogin.setOnClickListener(this)
        tvCadastrar.setOnClickListener(this)
    }

    private fun handleLogin() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        if(mUserBusiness.login(User(email = email, password = password))) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, getString(R.string.login_invalido), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.btLogin -> {
                handleLogin()
            }
            R.id.tvCadastrar -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }
}
