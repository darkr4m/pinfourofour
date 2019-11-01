package com.jtv.pinfourofour.responses.board

import com.google.gson.annotations.SerializedName

data class Boards(@SerializedName("data") val boards: List<Board>,
                  @SerializedName("page") val nextPage: BoardPage?) : Iterable<Board> {

    override fun iterator(): Iterator<Board>
            = boards.iterator()
}