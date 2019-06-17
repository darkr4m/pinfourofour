package com.jtv.pinfourofour.responses

import com.google.gson.annotations.SerializedName
import com.jtv.pinfourofour.responses.pin.Pin

data class ErrorResponse(
        val status: String?,
        val code: Int,
        val host: String?,
        @SerializedName("generated_at") val generatedAt: String?,
        val message: String?,
        val data: Pin?)
