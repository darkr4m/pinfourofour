package com.jtv.pinfourofour.responses.pin

import com.google.gson.annotations.SerializedName

data class Article(
        @SerializedName("published_at") val publishedAt: String?,
        val description: String?,
        val name: String?,
        val authors: List<Author>?)