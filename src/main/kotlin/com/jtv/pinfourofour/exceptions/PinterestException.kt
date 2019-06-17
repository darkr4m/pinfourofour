package com.jtv.pinfourofour.exceptions

class PinterestException : RuntimeException {

    constructor() : super()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}
