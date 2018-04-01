package br.com.task.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.task.R
import br.com.task.business.UserBusiness
import br.com.task.model.entity.User
import br.com.task.util.ValidationException
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListener()

        mUserBusiness = UserBusiness(this)
    }

    private fun setListener() {
        btRegister.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btRegister -> {
                handleSave()
            }
        }
    }

    private fun handleSave() {
        try {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (mUserBusiness.insert(User(name = name, email = email, password = password))) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.algo_errado_aconteceu), Toast.LENGTH_SHORT).show()
            }
        } catch (e: ValidationException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.algo_errado_aconteceu), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(): Boolean {
        return edtName.text.toString() != ""
                && edtEmail.text.toString() != ""
                && edtPassword.text.toString() != ""
    }
}
