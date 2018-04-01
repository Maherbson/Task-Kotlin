package br.com.task.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.task.R
import br.com.task.model.`interface`.ITaskListener
import br.com.task.model.entity.Task
import br.com.task.view.holder.TaskViewHolder

/**
 * Created by maher on 25/03/2018.
 */

class TaskListAdapter(var context: Context, val lTask: List<Task>, val iTaskListener: ITaskListener) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_task_list, parent, false)
        return TaskViewHolder(view, iTaskListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var task: Task = lTask[position]

        holder.bind(task)
    }


    override fun getItemCount(): Int {
        return lTask.size
    }
}