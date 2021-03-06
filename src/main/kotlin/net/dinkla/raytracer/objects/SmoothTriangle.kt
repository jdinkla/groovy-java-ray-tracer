package net.dinkla.raytracer.objects

import net.dinkla.raytracer.hits.Hit
import net.dinkla.raytracer.hits.ShadowHit
import net.dinkla.raytracer.math.*
import net.dinkla.raytracer.utilities.equals
import net.dinkla.raytracer.interfaces.hash

class SmoothTriangle(val v0: Point3D, val v1: Point3D, val v2: Point3D) : GeometricObject() {

    var n0: Normal = Normal.UP
    var n1: Normal = Normal.UP
    var n2: Normal = Normal.UP

    init {
        boundingBox = BBox.create(v0, v1, v2)
    }

    override fun hit(ray: Ray, sr: Hit): Boolean {
        val a = v0.x - v1.x
        val b = v0.x - v2.x
        val c = ray.direction.x
        val d = v0.x - ray.origin.x

        val e = v0.y - v1.y
        val f = v0.y - v2.y
        val g = ray.direction.y
        val h = v0.y - ray.origin.y

        val i = v0.z - v1.z
        val j = v0.z - v2.z
        val k = ray.direction.z
        val l = v0.z - ray.origin.z

        val m = f * k - g * j
        val n = h * k - g * l
        val p = f * l - h * j
        val q = g * i - e * k
        val s = e * j - f * i

        val invDenom = 1.0 / (a * m + b * q + c * s)

        val e1 = d * m - b * n - c * p
        val beta = e1 * invDenom

        if (beta < 0) {
            return false
        }

        val r = e * l - h * i
        val e2 = a * n + d * q + c * r
        val gamma = e2 * invDenom

        if (gamma < 0) {
            return false
        }

        if (beta + gamma > 1) {
            return false
        }

        val e3 = a * p - b * r + d * s
        val t = e3 * invDenom

        if (t < MathUtils.K_EPSILON) {
            return false
        }
        sr.t = t
        sr.normal = interpolateNormal(beta, gamma)
        //sr.localHitPoint = ray.linear(t);

        return true
    }

    private fun interpolateNormal(beta: Double, gamma: Double): Normal {
        val v1 = n0.times(1.0 - beta - gamma)
        val v2 = n1.times(beta)
        val v3 = n2.times(gamma)
        val normal = Normal(v1.plus(v2).plus(v3))
        return normal.normalize()
    }

    override fun shadowHit(ray: Ray, tmin: ShadowHit): Boolean {
        val a = v0.x - v1.x
        val b = v0.x - v2.x
        val c = ray.direction.x
        val d = v0.x - ray.origin.x

        val e = v0.y - v1.y
        val f = v0.y - v2.y
        val g = ray.direction.y
        val h = v0.y - ray.origin.y

        val i = v0.z - v1.z
        val j = v0.z - v2.z
        val k = ray.direction.z
        val l = v0.z - ray.origin.z

        val m = f * k - g * j
        val n = h * k - g * l
        val p = f * l - h * j
        val q = g * i - e * k
        val s = e * j - f * i

        val invDenom = 1.0 / (a * m + b * q + c * s)

        val e1 = d * m - b * n - c * p
        val beta = e1 * invDenom

        if (beta < 0) {
            return false
        }

        val r = e * l - h * i
        val e2 = a * n + d * q + c * r
        val gamma = e2 * invDenom

        if (gamma < 0) {
            return false
        }

        if (beta + gamma > 1) {
            return false
        }

        val e3 = a * p - b * r + d * s
        val t = e3 * invDenom

        if (t < MathUtils.K_EPSILON) {
            return false
        }
        tmin.t = t

        return true
    }

    override fun equals(other: Any?): Boolean = this.equals<SmoothTriangle>(other) { a, b ->
        a.v0 == b.v0 && a.v1 == b.v1 && a.v2 == b.v2
    }

    override fun hashCode(): Int = this.hash(v0, v1, v2)

    override fun toString(): String = "SmoothTriangle($v0, $v1, $v2)"
}
