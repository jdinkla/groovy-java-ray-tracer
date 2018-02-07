package net.dinkla.raytracer.math

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 05.05.2010
 * Time: 21:20:07
 * To change this template use File | Settings | File Templates.
 */
object Polynomials {

    fun solveQuadric(c: FloatArray, s: FloatArray): Int {
        assert(c.size == 3)
        assert(s.size == 2)

        /* normal form: x^2 + px + q = 0 */
        val p = c[1] / (2 * c[2])
        val q = c[0] / c[2]
        val D = p * p - q

        if (MathUtils.isZero(D)) {
            s[0] = -p
            return 1
        } else if (D > 0) {
            val sqrtD = Math.sqrt(D.toDouble()).toFloat()
            s[0] = sqrtD - p
            s[1] = -sqrtD - p
            return 2
        } else {
            return 0
        }
    }

    fun solveQuartic(c: FloatArray, s: FloatArray): Int {
        assert(c.size == 5)
        assert(s.size == 4)

        val coeffs4 = FloatArray(4)
        val coeffs3 = FloatArray(3)
        val z: Float
        var u: Float
        var v: Float
        val sub: Float
        val A: Float
        val B: Float
        val C: Float
        val D: Float
        val sq_A: Float
        val p: Float
        val q: Float
        val r: Float
        var i: Int
        var num: Int

        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */
        A = c[3] / c[4]
        B = c[2] / c[4]
        C = c[1] / c[4]
        D = c[0] / c[4]

        /*  substitute x = y - A/4 to eliminate cubic term:
            x^4 + px^2 + qx + r = 0 */
        sq_A = A * A
        p = -3.0f / 8 * sq_A + B
        q = 1.0f / 8 * sq_A * A - 1.0f / 2 * A * B + C
        r = -3.0f / 256 * sq_A * sq_A + 1.0f / 16 * sq_A * B - 1.0f / 4 * A * C + D

        if (MathUtils.isZero(r)) {
            /* no absolute term: y(y^3 + py + q) = 0 */
            coeffs4[0] = q
            coeffs4[1] = p
            coeffs4[2] = 0f
            coeffs4[3] = 1f
            val ss0 = floatArrayOf(s[0], s[1], s[2])
            num = solveCubic(coeffs4, ss0)
            s[0] = ss0[0]
            s[1] = ss0[1]
            s[2] = ss0[2]
            s[num++] = 0f
        } else {
            /* solve the resolvent cubic ... */
            coeffs4[0] = 1.0f / 2 * r * p - 1.0f / 8 * q * q
            coeffs4[1] = -r
            coeffs4[2] = -1.0f / 2 * p
            coeffs4[3] = 1f

            val ss1 = floatArrayOf(s[0], s[1], s[2])
            solveCubic(coeffs4, ss1)
            s[0] = ss1[0]
            s[1] = ss1[1]
            s[2] = ss1[2]

            /* ... and take the one real solution ... */
            z = s[0]

            /* ... to build two quadric equations */
            u = z * z - r
            v = 2 * z - p

            if (MathUtils.isZero(u)) {
                u = 0f
            } else if (u > 0) {
                u = Math.sqrt(u.toDouble()).toFloat()
            } else {
                return 0
            }
            if (MathUtils.isZero(v)) {
                v = 0f
            } else if (v > 0) {
                v = Math.sqrt(v.toDouble()).toFloat()
            } else {
                return 0
            }
            coeffs3[0] = z - u
            coeffs3[1] = if (q < 0) -v else v
            coeffs3[2] = 1f

            val ss2 = floatArrayOf(s[0], s[1])
            num = solveQuadric(coeffs3, ss2)
            s[0] = ss2[0]
            s[1] = ss2[1]

            coeffs3[0] = z + u
            coeffs3[1] = if (q < 0) v else -v
            coeffs3[2] = 1f

            val ss3 = floatArrayOf(s[0 + num], s[1 + num])
            num += Polynomials.solveQuadric(coeffs3, ss3)
            s[0] = ss3[0]
            s[1] = ss3[1]

        }

        /* resubstitute */
        sub = 1.0f / 4 * A

        i = 0
        while (i < num) {
            s[i] -= sub
            ++i
        }
        return num
    }

    fun solveCubic(c: FloatArray, s: FloatArray): Int {
        assert(c.size == 4)
        assert(s.size == 3)

        var i: Int
        val num: Int
        val sub: Float
        val A: Float
        val B: Float
        val C: Float
        val sq_A: Float
        val p: Float
        val q: Float
        val cb_p: Float
        val D: Float

        /* normal form: x^3 + Ax^2 + Bx + C = 0 */
        A = c[2] / c[3]
        B = c[1] / c[3]
        C = c[0] / c[3]

        /*  substitute x = y - A/3 to eliminate quadric term: x^3 +px + q = 0 */
        sq_A = A * A
        p = 1.0f / 3 * (-1.0f / 3 * sq_A + B)
        q = 1.0f / 2 * (2.0f / 27 * A * sq_A - 1.0f / 3 * A * B + C)

        /* use Cardano's formula */
        cb_p = p * p * p
        D = q * q + cb_p

        if (MathUtils.isZero(D)) {
            if (MathUtils.isZero(q)) { /* one triple solution */
                s[0] = 0f
                num = 1
            } else { /* one single and one double solution */
                val u = Math.cbrt((-q).toDouble()).toFloat()
                s[0] = 2 * u
                s[1] = -u
                num = 2
            }
        } else if (D < 0) { /* Casus irreducibilis: three real solutions */
            val phi = 1.0f / 3 * Math.acos((-q / Math.sqrt((-cb_p).toDouble()).toFloat()).toDouble()).toFloat()
            val t = 2 * Math.sqrt((-p).toDouble()).toFloat()

            s[0] = t * Math.cos(phi.toDouble()).toFloat()
            s[1] = -t * Math.cos(phi + Math.PI / 3).toFloat()
            s[2] = -t * Math.cos(phi - Math.PI / 3).toFloat()

            num = 3
        } else { /* one real solution */
            val sqrt_D = Math.sqrt(D.toDouble()).toFloat()
            val u = Math.cbrt((sqrt_D - q).toDouble()).toFloat()
            val v = -Math.cbrt((sqrt_D + q).toDouble()).toFloat()
            s[0] = u + v
            num = 1
        }

        /* resubstitute */
        sub = 1.0f / 3 * A
        i = 0
        while (i < num) {
            s[i] -= sub
            ++i
        }
        return num
    }

    fun solveQuadric(c: DoubleArray, s: DoubleArray): Int {
        assert(c.size == 3)
        assert(s.size == 2)

        /* normal form: x^2 + px + q = 0 */
        val p = c[1] / (2 * c[2])
        val q = c[0] / c[2]
        val D = p * p - q

        if (MathUtils.isZero(D)) {
            s[0] = -p
            return 1
        } else if (D > 0) {
            val sqrtD = Math.sqrt(D).toFloat().toDouble()
            s[0] = sqrtD - p
            s[1] = -sqrtD - p
            return 2
        } else {
            return 0
        }
    }

    fun solveQuartic(c: DoubleArray, s: DoubleArray): Int {
        assert(c.size == 5)
        assert(s.size == 4)

        val coeffs4 = DoubleArray(4)
        val coeffs3 = DoubleArray(3)
        val z: Double
        var u: Double
        var v: Double
        val sub: Double
        val A: Double
        val B: Double
        val C: Double
        val D: Double
        val sq_A: Double
        val p: Double
        val q: Double
        val r: Double
        var i: Int
        var num: Int

        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */
        A = c[3] / c[4]
        B = c[2] / c[4]
        C = c[1] / c[4]
        D = c[0] / c[4]

        /*  substitute x = y - A/4 to eliminate cubic term:
            x^4 + px^2 + qx + r = 0 */
        sq_A = A * A
        p = -3.0f / 8 * sq_A + B
        q = (1.0f / 8).toDouble() * sq_A * A - (1.0f / 2).toDouble() * A * B + C
        r = (-3.0f / 256).toDouble() * sq_A * sq_A + (1.0f / 16).toDouble() * sq_A * B - (1.0f / 4).toDouble() * A * C + D

        if (MathUtils.isZero(r)) {
            /* no absolute term: y(y^3 + py + q) = 0 */
            coeffs4[0] = q
            coeffs4[1] = p
            coeffs4[2] = 0.0
            coeffs4[3] = 1.0
            val ss = doubleArrayOf(s[0], s[1], s[2])
            num = solveCubic(coeffs4, ss)
            s[0] = ss[0]
            s[1] = ss[1]
            s[2] = ss[2]
            s[num++] = 0.0
        } else {
            /* solve the resolvent cubic ... */
            coeffs4[0] = (1.0f / 2).toDouble() * r * p - (1.0f / 8).toDouble() * q * q
            coeffs4[1] = -r
            coeffs4[2] = -1.0f / 2 * p
            coeffs4[3] = 1.0

            val ss = doubleArrayOf(s[0], s[1], s[2])
            solveCubic(coeffs4, ss)
            s[0] = ss[0]
            s[1] = ss[1]
            s[2] = ss[2]

            /* ... and take the one real solution ... */
            z = s[0]

            /* ... to build two quadric equations */
            u = z * z - r
            v = 2 * z - p

            if (MathUtils.isZero(u)) {
                u = 0.0
            } else if (u > 0) {
                u = Math.sqrt(u).toFloat().toDouble()
            } else {
                return 0
            }
            if (MathUtils.isZero(v)) {
                v = 0.0
            } else if (v > 0) {
                v = Math.sqrt(v).toFloat().toDouble()
            } else {
                return 0
            }
            coeffs3[0] = z - u
            coeffs3[1] = if (q < 0) -v else v
            coeffs3[2] = 1.0

            val ss2 = doubleArrayOf(s[0], s[1])
            num = solveQuadric(coeffs3, ss2)
            s[0] = ss2[0]
            s[1] = ss2[1]

            coeffs3[0] = z + u
            coeffs3[1] = if (q < 0) v else -v
            coeffs3[2] = 1.0

            // TODO: Was heißt s+ num
            //            float[] ss3 = { s[0 + num], s[1 + num] };
            val ss3 = doubleArrayOf(s[0 + num], s[1 + num])
            num += Polynomials.solveQuadric(coeffs3, ss3)
            s[0] = ss3[0]
            s[1] = ss3[1]

        }

        /* resubstitute */
        sub = 1.0f / 4 * A

        i = 0
        while (i < num) {
            s[i] -= sub
            ++i
        }
        return num
    }

    fun solveCubic(c: DoubleArray, s: DoubleArray): Int {
        assert(c.size == 4)
        assert(s.size == 3)

        var i: Int
        val num: Int
        val sub: Double
        val A: Double
        val B: Double
        val C: Double
        val sq_A: Double
        val p: Double
        val q: Double
        val cb_p: Double
        val D: Double

        /* normal form: x^3 + Ax^2 + Bx + C = 0 */
        A = c[2] / c[3]
        B = c[1] / c[3]
        C = c[0] / c[3]

        /*  substitute x = y - A/3 to eliminate quadric term: x^3 +px + q = 0 */
        sq_A = A * A
        p = 1.0f / 3 * (-1.0f / 3 * sq_A + B)
        q = 1.0f / 2 * ((2.0f / 27).toDouble() * A * sq_A - (1.0f / 3).toDouble() * A * B + C)

        /* use Cardano's formula */
        cb_p = p * p * p
        D = q * q + cb_p

        if (MathUtils.isZero(D)) {
            if (MathUtils.isZero(q)) { /* one triple solution */
                s[0] = 0.0
                num = 1
            } else { /* one single and one double solution */
                val u = Math.cbrt(-q)
                s[0] = 2 * u
                s[1] = -u
                num = 2
            }
        } else if (D < 0) { /* Casus irreducibilis: three real solutions */
            val phi = 1.0f / 3 * Math.acos(-q / Math.sqrt(-cb_p))
            val t = 2 * Math.sqrt(-p)

            s[0] = t * Math.cos(phi)
            s[1] = -t * Math.cos(phi + Math.PI / 3)
            s[2] = -t * Math.cos(phi - Math.PI / 3)

            num = 3
        } else { /* one real solution */
            val sqrt_D = Math.sqrt(D)
            val u = Math.cbrt(sqrt_D - q)
            val v = -Math.cbrt(sqrt_D + q)
            s[0] = u + v
            num = 1
        }

        /* resubstitute */
        sub = 1.0f / 3 * A
        i = 0
        while (i < num) {
            s[i] -= sub
            ++i
        }
        return num
    }

}