package es.informaticamovil.tanteo

import android.os.Parcel
import android.os.Parcelable

class TeamPoints (var de1: Int = 0, var de2: Int = 0, var de3: Int = 0): Parcelable {

    val total get() = de1 + 2 * de2 + 3 * de3;

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(de1)
        parcel.writeInt(de2)
        parcel.writeInt(de3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeamPoints> {
        override fun createFromParcel(parcel: Parcel): TeamPoints {
            return TeamPoints(parcel)
        }

        override fun newArray(size: Int): Array<TeamPoints?> {
            return arrayOfNulls(size)
        }
    }
}