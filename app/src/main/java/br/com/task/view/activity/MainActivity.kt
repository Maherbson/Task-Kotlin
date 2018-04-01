package br.com.task.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.com.task.R
import br.com.task.business.PriorityBusiness
import br.com.task.constants.SharedPrefConstants
import br.com.task.constants.TaskConstants
import br.com.task.database.PriorityCacheConstants
import br.com.task.util.SharedPref
import br.com.task.view.fragment.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mSharedPref: SharedPref
    private lateinit var mPriorityBusiness: PriorityBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mSharedPref = SharedPref(this)
        mPriorityBusiness = PriorityBusiness(this)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setListPriorityCache()
        setFragment(TaskListFragment.newInstance(TaskConstants.TASKFILTER.COMPLETE))
        formatUserName()
        formatDate()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.nav_done -> {
                setFragment(TaskListFragment.newInstance(TaskConstants.TASKFILTER.COMPLETE))
            }
            R.id.nav_todo -> {
                setFragment(TaskListFragment.newInstance(TaskConstants.TASKFILTER.TODO))
            }
            R.id.nav_logout -> {
                logout()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setListPriorityCache() {
        PriorityCacheConstants.setCache(mPriorityBusiness.getList())
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit()
    }

    private fun logout() {
        mSharedPref.deleteStoreString(SharedPrefConstants.KEY.USER_ID)
        mSharedPref.deleteStoreString(SharedPrefConstants.KEY.USER_NAME)
        mSharedPref.deleteStoreString(SharedPrefConstants.KEY.USER_EMAIL)

        startActivity(Intent(this, LoginActivity::class.java))
    }


    private fun formatUserName() {
        val hello = "Olá, ${mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_NAME)}!"
        tvHelloUser.text = hello

        val navigationView = nav_view as NavigationView
        val header = navigationView.getHeaderView(0)

        header.tvNameUser.text = mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_NAME)
        header.tvEmailUser.text = mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_EMAIL)
    }

    private fun formatDate() {
        val days = arrayOf("Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado")
        val months = arrayOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")

        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val dayWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val month = calendar.get(Calendar.MONTH)

        val descriptionDate = "${days[dayWeek]}, ${day} de ${months[month]}"
        tvDescriptionDate.text = descriptionDate
    }
}
