package br.com.task.model.`interface`

import br.com.task.model.entity.Task
import java.util.*

/**
 * Created by maher on 30/03/2018.
 */
interface ITaskListener {
    fun onListenerClick(objects: Any)
    fun onListenerLongClick(objects: Any)
    fun onTaskCompleteClick(objects: Any)
    fun onTaskUnCompleteClick(objects: Any)
}