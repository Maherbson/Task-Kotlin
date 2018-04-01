package br.com.task.business

import android.content.Context
import br.com.task.R
import br.com.task.constants.SharedPrefConstants
import br.com.task.database.TaskRepository
import br.com.task.model.entity.Task
import br.com.task.model.entity.User
import br.com.task.util.SharedPref
import br.com.task.util.ValidationException

/**
 * Created by maher on 24/03/2018.
 */
class TaskBusiness(var context: Context) {

    private val mTaskRepository: TaskRepository = TaskRepository.getInstance(context)
    private val mSharedPref: SharedPref = SharedPref(context)

    fun getListTaskByUserId(mTaskFilter: Int): MutableList<Task> = mTaskRepository.getListTaskByUserId(getUser(), mTaskFilter)

    fun insertTask(task: Task): Boolean {
        return if (task.userId == 0
                || task.description == ""
                || task.dueDate == context.getString(R.string.selecione_a_data)
                || task.priority == -1) {
            throw ValidationException(context.getString(R.string.campos_vazios))
        } else {
            mTaskRepository.insertTask(task)
            true
        }
    }

    fun updateTaskById(task: Task): Boolean {
        return if (task.userId == 0
                || task.description == ""
                || task.dueDate == context.getString(R.string.selecione_a_data)
                || task.priority == -1) {
            throw ValidationException(context.getString(R.string.campos_vazios))
        } else {
            mTaskRepository.updateTaskById(task)
            true
        }
    }

    fun deleteTaskById(task: Task) {
        mTaskRepository.deleteTaskById(task)
    }


    fun complete(task: Task, complete: Boolean) {
        val task = mTaskRepository.getTaskById(task)

        if (task != null) {
            task.complete = complete
            mTaskRepository.updateTaskById(task)
        }
    }

    private fun getUser(): User = User(mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_ID).toInt(),
            mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_NAME),
            mSharedPref.getStoreString(SharedPrefConstants.KEY.USER_EMAIL))

}