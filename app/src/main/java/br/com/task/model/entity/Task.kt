package br.com.task.model.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by maher on 24/03/2018.
 */

data class Task(var id: Int = -1,
                var userId: Int,
                var priority: Int,
                var description: String,
                var dueDate: String,
                var complete: Boolean): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(userId)
        parcel.writeInt(priority)
        parcel.writeString(description)
        parcel.writeString(dueDate)
        parcel.writeByte(if (complete) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}