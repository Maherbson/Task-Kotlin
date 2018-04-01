package br.com.task.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.task.R
import br.com.task.business.TaskBusiness
import br.com.task.constants.TaskConstants
import br.com.task.model.`interface`.ITaskListener
import br.com.task.model.entity.Task
import br.com.task.view.activity.TaskFormActivity
import br.com.task.view.adapter.TaskListAdapter
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import java.util.*

class TaskListFragment : Fragment(), View.OnClickListener {

    private lateinit var mRcListTask: RecyclerView
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var iTaskLister: ITaskListener

    private var mTaskFilter: Int = 0

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(taskFilter: Int): TaskListFragment {
            val fragment = TaskListFragment()
            val bundle = Bundle()
            bundle.putInt(TaskConstants.TASKFILTER.KEY, taskFilter)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTaskFilter = arguments.getInt(TaskConstants.TASKFILTER.KEY, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_task_list, container, false)
        configureView(view)
        instanceBusiness()

        iTaskLister = object : ITaskListener {
            override fun onListenerClick(objects: Any) {
                val task: Task? = objects as Task

                val intent = Intent(context, TaskFormActivity::class.java)
                intent.putExtra(TaskConstants.BUNDLE.OBJECT_TASK, task)
                startActivity(intent)
            }

            override fun onListenerLongClick(objects: Any) {
                val task: Task = objects as Task
                mTaskBusiness.deleteTaskById(task)
                loadAdapter()
                Toast.makeText(context, getString(R.string.tafera_removida), Toast.LENGTH_SHORT).show()
            }

            override fun onTaskUnCompleteClick(objects: Any) {
                val task: Task = objects as Task
                mTaskBusiness.complete(task, false)
                loadAdapter()
            }

            override fun onTaskCompleteClick(objects: Any) {
                val task: Task = objects as Task
                mTaskBusiness.complete(task, true)
                loadAdapter()
            }
        }

        return view
    }

    private fun loadAdapter() {
        var qtdList: Int = 0

        val lTask = mTaskBusiness.getListTaskByUserId(mTaskFilter)

        if (lTask.size > 0) {
            qtdList = lTask.size + 1
        }

        mRcListTask.setItemViewCacheSize(qtdList)
        mRcListTask.adapter = TaskListAdapter(context, lTask, iTaskLister)
        mRcListTask.adapter.notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fabAddTask -> {
                startActivity(Intent(context, TaskFormActivity::class.java))
            }
        }
    }

    private fun configureView(view: View) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        mRcListTask = view.rcListTask
        mRcListTask.setHasFixedSize(true)
        mRcListTask.layoutManager = linearLayoutManager

        view.fabAddTask.setOnClickListener(this)
    }

    private fun instanceBusiness() {
        mTaskBusiness = TaskBusiness(context)
    }

    override fun onResume() {
        super.onResume()
        loadAdapter()
    }
}