package com.jtv.pinfourofour.fields.pin

import com.jtv.pinfourofour.fields.BaseFields
import com.jtv.pinfourofour.fields.Fields

class BoardPinFields : BaseFields() {

    override fun withAll(): Fields
            = this.withID().withName().withURL().withCounts()

    fun withID(): BoardPinFields {
        fields = fields.plus("id")
        return this
    }

    fun withName(): BoardPinFields {
        fields = fields.plus("name")
        return this
    }

    fun withURL(): BoardPinFields {
        fields = fields.plus("url")
        return this
    }

    fun withCounts(): BoardPinFields {
        fields = fields.plus("counts")
        return this
    }
}
