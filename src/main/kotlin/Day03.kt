object Day03 : Day {
    override fun List<String>.part1(): Int = priorities()

    override fun List<String>.part2(): Int = priorities3()
}

fun List<String>.priorities(): Int = 
    map { it.chunked(it.count()/2) }
    .map { it.first().toList().intersect(it.get(1).toList()).first()  }
    .map { 
        it.toInt() - (if(it.isUpperCase()) (64 -26) else 96 )
    }
    .sum()


fun List<String>.priorities3(): Int = 
    chunked(3)
    .map { 
        val first = it.first().toList()
        val second = it.get(1).toList()
        val third = it.get(2).toList()

       first.intersect(second).intersect(third).first()
    }
    .map { 
        it.toInt() - (if(it.isUpperCase()) (64 -26) else 96 )
    }
    .sum()
    