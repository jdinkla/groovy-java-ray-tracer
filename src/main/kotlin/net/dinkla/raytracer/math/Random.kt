package net.dinkla.raytracer.math

object Random {

    private val r = java.util.Random()

    fun int(high: Int): Int = r.nextInt(high)

    fun int(low: Int, high: Int): Int = r.nextInt(high - low) + low

    // TODO rename double
    fun double(): Double = r.nextDouble()

    // TODO rename double
    fun double(low: Double, high: Double): Double = r.nextDouble() * (high - low) + low

    fun setRandSeed(seed: Int) {
        r.setSeed(seed.toLong())
    }

    fun randomShuffle(ls: MutableList<Int>) {
        val n = ls.size
        for (i in 1 until n) {
            val i2 = int(n)
            val tmp = ls[i]
            ls[i] = ls[i2]
            ls[i2] = tmp
        }
    }
}
