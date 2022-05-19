package com.wael.tyvolunty.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VolunteerCall (
    @SerializedName("_id") val _id :String,
    @SerializedName("callId") val callId :String,
    @SerializedName("idV") val idV :String,
    @SerializedName("status") val status :String,



    ): Serializable