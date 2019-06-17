package com.jtv.pinfourofour.responses.board

import com.google.gson.annotations.SerializedName
import com.jtv.pinfourofour.responses.SixtyBySixty

data class Image(@SerializedName("60x60") val sixtyBySixty: SixtyBySixty?)