package net.dinkla.raytracer.math

import java.util.*

class Histogram {

    internal var counts: MutableMap<Int, Int>

    init {
        counts = TreeMap()
    }

    fun add(key: Int) {
        counts.merge(key, 1) { a, b -> a + b }
    }

    operator fun get(key: Int): Int = counts[key] ?: 0

//    fun clear() {
//        counts.clear()
//    }

    fun keys(): Set<Int> = counts.keys

//    fun keySet(): Set<Int> {
//        return counts.keys
//    }

    fun println() {
        var min: Int = Integer.MAX_VALUE
        var max: Int = Integer.MIN_VALUE
        for (k in counts.keys) {
            val v = get(k)
            if (v > max) max = v
            if (v < min) min = v
            println("k=$k, v=$v")
        }
        if (min != Integer.MAX_VALUE || max != Integer.MIN_VALUE) {
            println("min=$min, max=$max")
        }
    }

}
