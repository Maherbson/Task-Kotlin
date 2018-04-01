package br.com.task.business

import android.content.Context
import br.com.task.database.PriorityRepository
import br.com.task.model.entity.Priority

/**
 * Created by maher on 23/03/2018.
 */
class PriorityBusiness(context: Context) {

    private val mPriorityRepository: PriorityRepository = PriorityRepository.getInstance(context)

    fun getList(): MutableList<Priority> = mPriorityRepository.getList()

}