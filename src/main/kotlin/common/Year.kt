package common

import kotlin.reflect.javaType

interface Year {
    @OptIn(ExperimentalStdlibApi::class)
    val year: String
        get() = this::class.supertypes.first().javaType.typeName.filter { it.isDigit() }

    val name: String?
        get() = this::class.simpleName
}

interface Year00 : Year

interface Year22 : Year

interface Year23 : Year

