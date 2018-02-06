package net.dinkla.raytracer.objects

import net.dinkla.raytracer.hits.Hit
import net.dinkla.raytracer.hits.ShadowHit
import net.dinkla.raytracer.math.*

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 10.04.2010
 * Time: 16:06:24
 * To change this template use File | Settings | File Templates.
 */
class Triangle(val v0: Point3D, val v1: Point3D, val v2: Point3D) : GeometricObject() {
    var normal: Normal

    init {
        normal = Normal(v0, v1, v2)
    }

    override fun hit(ray: Ray, sr: Hit): Boolean {

        val a = v0.x - v1.x
        val b = v0.x - v2.x
        val c = ray.d.x
        val d = v0.x - ray.o.x

        val e = v0.y - v1.y
        val f = v0.y - v2.y
        val g = ray.d.y
        val h = v0.y - ray.o.y

        val i = v0.z - v1.z
        val j = v0.z - v2.z
        val k = ray.d.z
        val l = v0.z - ray.o.z

        val m = f * k - g * j
        val n = h * k - g * l
        val p = f * l - h * j
        val q = g * i - e * k
        val s = e * j - f * i

        val invDenom = 1.0f / (a * m + b * q + c * s)

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
        sr.setT(t)
        sr.normal = normal

        return true
    }

    override fun shadowHit(ray: Ray, tmin: ShadowHit): Boolean {

        val a = v0.x - v1.x
        val b = v0.x - v2.x
        val c = ray.d.x
        val d = v0.x - ray.o.x

        val e = v0.y - v1.y
        val f = v0.y - v2.y
        val g = ray.d.y
        val h = v0.y - ray.o.y

        val i = v0.z - v1.z
        val j = v0.z - v2.z
        val k = ray.d.z
        val l = v0.z - ray.o.z

        val m = f * k - g * j
        val n = h * k - g * l
        val p = f * l - h * j
        val q = g * i - e * k
        val s = e * j - f * i

        val invDenom = 1.0f / (a * m + b * q + c * s)

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
        tmin.setT(t)

        return true
    }

    override fun getBoundingBox(): BBox {
        return BBox.create(v0, v1, v2)
    }
}
