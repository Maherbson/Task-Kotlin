package br.com.task.view.holder

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.task.R
import br.com.task.database.PriorityCacheConstants
import br.com.task.model.`interface`.ITaskListener
import br.com.task.model.entity.Task
import kotlinx.android.synthetic.main.cardview_task_list.view.*

/**
 * Created by maher on 25/03/2018.
 */
class TaskViewHolder(itemView: View, val iTaskListener: ITaskListener): RecyclerView.ViewHolder(itemView) {

    fun bind(task: Task) {
        itemView.tvDescription.text = task.description
        itemView.tvPriority.text = PriorityCacheConstants.getCache(task.priority)
        itemView.tvDate.text = task.dueDate

        if (task.complete) {
            itemView.ivCheck.setImageResource(R.drawable.ic_done)
        }

        itemView.setOnClickListener({
            iTaskListener.onListenerClick(task)
        })

        itemView.setOnLongClickListener( {
            callDialog(task)
            true
        })

        itemView.ivCheck.setOnClickListener({
            if (task.complete) {
                iTaskListener.onTaskUnCompleteClick(task)
            } else {
                iTaskListener.onTaskCompleteClick(task)
            }
        })
    }

    private fun callDialog(task: Task) {
        AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remover_tarefa)
                .setMessage(String.format(itemView.context.getString(R.string.deseja_remover), task.description))
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton(itemView.context.getString(R.string.remover), {dialogInterface, i -> iTaskListener.onListenerLongClick(task) })
                .setNegativeButton(itemView.context.getString(R.string.cancelar), null)
                .show()
    }
}