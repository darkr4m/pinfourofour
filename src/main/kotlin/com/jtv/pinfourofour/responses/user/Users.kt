package com.jtv.pinfourofour.responses.user

import com.google.gson.annotations.SerializedName

data class Users(@SerializedName("data") val users: List<User>,
                 @SerializedName("page") val nextPage: UserPage?) : Iterable<User> {

    override fun iterator(): Iterator<User>
        = users.iterator()
}