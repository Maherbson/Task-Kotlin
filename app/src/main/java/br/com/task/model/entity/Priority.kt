package br.com.task.model.entity

/**
 * Created by maher on 23/03/2018.
 */
data class Priority(val id: Int,
                    val description: String) {

    override fun toString(): String {
        return description
    }
}