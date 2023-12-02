package common

interface Year {
    val year: String

    fun name(): String? = this::class.simpleName
}

abstract class Year00 : Year {
    override val year: String = "00"
}

abstract class Year22 : Year {
    override val year: String = "22"
}

abstract class Year23 : Year {
    override val year: String = "23"
}

