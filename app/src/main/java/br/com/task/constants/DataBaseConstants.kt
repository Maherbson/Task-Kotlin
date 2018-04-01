package br.com.task.constants

/**
 * Created by maher on 20/03/2018.
 */
class DataBaseConstants {

    object User {
        val TABLE_NAME = "user"

        object COLUMNS {
            val ID = "id"
            val NAME = "name"
            val EMAIL = "email"
            val PASSWORD = "password"
        }
    }

    object PRIORITY {
        val TABLE_NAME = "priority"

        object COLUMNS {
            val ID = "id"
            val DESCRIPTION = "description"
        }
    }

    object TASK {
        val TABLE_NAME = "task"

        object COLUMNS {
            val ID = "id"
            val USERID = "userid"
            val PRIORITYID = "priorityid"
            val DESCRIPTION = "description"
            val COMPLETE = "complete"
            val DUEDATE = "duedate"
        }
    }
}