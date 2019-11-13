package com.gylee.ipcdemo

import android.os.Parcel
import android.os.Parcelable

data class Book2(val id: Int, val name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Book2> {
        override fun createFromParcel(parcel: Parcel): Book2 {
            return Book2(parcel)
        }

        override fun newArray(size: Int): Array<Book2?> {
            return arrayOfNulls(size)
        }
    }

}