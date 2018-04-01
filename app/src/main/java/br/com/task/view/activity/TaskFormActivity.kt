package br.com.task.view.activity

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import br.com.task.R
import br.com.task.business.PriorityBusiness
import br.com.task.business.TaskBusiness
import br.com.task.constants.SharedPrefConstants
import br.com.task.constants.TaskConstants
import br.com.task.model.entity.Priority
import br.com.task.model.entity.Task
import br.com.task.util.SharedPref
import br.com.task.util.ValidationException
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSharedPref: SharedPref
    private lateinit var mLPriority: MutableList<Priority>

    private val mSimpleDate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var bundle: Task? = null

    private var idPrioritySelected: Int = 0

    private var mTaskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mPriorityBusiness = PriorityBusiness(this)
        mTaskBusiness = TaskBusiness(this)
        mSharedPref = SharedPref(this)

        setListener()
        loadPriorities()
        loadTask()
    }

    private fun loadTask() {
        if (intent.extras != null) {
            bundle = intent.extras.getParcelable<Task>(TaskConstants.BUNDLE.OBJECT_TASK) as Task
            if (bundle != null) {
                mTaskId = bundle!!.id
                edtDescription.setText(bundle!!.description)
                btSelectDate.text = bundle!!.dueDate
                cbComplete.isChecked = bundle!!.complete
                spPriority.setSelection(getPosition(bundle!!.priority))

                btSave.text = getString(R.string.atualizar_tarefa)
            }
        }

    }

    private fun getPosition(priority: Int): Int {
        var position = 0
        for (i in 0..mLPriority.size) {
            if (priority == mLPriority[i].id) {
                position = i
                break
            }
        }

        return position
    }

    private fun setListener() {
        btSelectDate.setOnClickListener(this)
        btSave.setOnClickListener(this)

        spPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, positon: Int, id: Long) {
                var priority: Priority = adapterView.adapter.getItem(positon) as Priority
                idPrioritySelected = priority.id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.btSelectDate -> {
                openDatePickerDialog()
            }
            R.id.btSave -> {
                handleSave()
            }
        }
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        btSelectDate.text = mSimpleDate.format(calendar.time)
    }

    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun loadPriorities() {
        mLPriority = mPriorityBusiness.getList()
        val adapter = ArrayAdapter<Priority>(this, android.R.layout.simple_spinner_dropdown_item, mLPriority)
        spPriority.adapter = adapter
    }

    private fun handleSave() {
        try {
            val description = edtDescription.text.toString()
            val complete = cbComplete.isChecked
            val dueDate = btSelectDate.text.toString()
            val userId = mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_ID)

            val task: Task = Task(mTaskId, userId = if (userId == "") 0 else userId.toInt(),
                                  priority = idPrioritySelected,
                                  description = description,
                                  dueDate = dueDate,
                                  complete = complete)

            if (mTaskId == -1) {
                mTaskBusiness.insertTask(task)
                Toast.makeText(this, getString(R.string.tarefa_inserida), Toast.LENGTH_SHORT).show()
            } else {
                mTaskBusiness.updateTaskById(task)
                Toast.makeText(this, getString(R.string.tarefa_atualizada), Toast.LENGTH_SHORT).show()
            }
            finish()

        } catch (e: ValidationException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
