package br.com.task.database

import br.com.task.model.entity.Priority

/**
 * Created by maher on 29/03/2018.
 */
class PriorityCacheConstants private constructor(){

    companion object {
        private val mHashPriority = hashMapOf<Int, String>()

        fun setCache(list: List<Priority>) {
            for (item in list) {
                mHashPriority.put(item.id, item.description)
            }
        }

        fun getCache(id: Int): String {
            if (mHashPriority[id] == null) {
                return ""
            }

            return mHashPriority[id].toString()
        }
    }
}