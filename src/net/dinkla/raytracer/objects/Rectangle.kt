package net.dinkla.raytracer.objects

import net.dinkla.raytracer.hits.Hit
import net.dinkla.raytracer.hits.Shade
import net.dinkla.raytracer.hits.ShadowHit
import net.dinkla.raytracer.lights.ILightSource
import net.dinkla.raytracer.math.*

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 12.04.2010
 * Time: 19:21:22
 * To change this template use File | Settings | File Templates.
 */
open class Rectangle : GeometricObject {

    val p0: Point3D
    val a: Vector3D
    val b: Vector3D
    val normal: Normal

    constructor(p0: Point3D, a: Vector3D, b: Vector3D) {
        this.p0 = p0
        this.a = a
        this.b = b
        val v = a.cross(b)
        normal = Normal(v.normalize())
    }

    constructor(p0: Point3D, a: Vector3D, b: Vector3D, inverted: Boolean) {
        this.p0 = p0
        this.a = a
        this.b = b
        val v: Vector3D
        if (inverted) {
            v = b.cross(a)
        } else {
            v = a.cross(b)
        }
        normal = Normal(v.normalize())
    }

    constructor(p0: Point3D, a: Vector3D, b: Vector3D, normal: Normal) {
        this.p0 = p0
        this.a = a
        this.b = b
        this.normal = normal
    }

    override fun hit(ray: Ray, sr: Hit): Boolean {
        val nom = p0.minus(ray.o).dot(normal)
        val denom = ray.d.dot(normal)
        val t = nom / denom

        if (t <= MathUtils.K_EPSILON) {
            return false
        }

        val p = ray.linear(t)
        val d = p.minus(p0)

        val ddota = d.dot(a)
        if (ddota < 0 || ddota > a.sqrLength()) {
            return false
        }

        val ddotb = d.dot(b)
        if (ddotb < 0 || ddotb > b.sqrLength()) {
            return false
        }

        sr.setT(t)
        sr.normal = normal

        return true
    }

    override fun shadowHit(ray: Ray, tmin: ShadowHit): Boolean {
        val nom = p0.minus(ray.o).dot(normal)
        val denom = ray.d.dot(normal)
        val t = nom / denom

        if (t <= MathUtils.K_EPSILON) {
            return false
        }

        val p = ray.linear(t)
        val d = p.minus(p0)

        val ddota = d.dot(a)
        if (ddota < 0 || ddota > a.sqrLength()) {
            return false
        }

        val ddotb = d.dot(b)
        if (ddotb < 0 || ddotb > b.sqrLength()) {
            return false
        }

        tmin.setT(t)
        return true
    }

    fun getNormal(p: Point3D): Normal {
        return normal
    }

    override fun getBoundingBox(): BBox {
        val v0 = p0
        val v1 = p0.plus(a).plus(b)

        var x0 = java.lang.Float.POSITIVE_INFINITY
        var x1 = java.lang.Float.NEGATIVE_INFINITY
        if (v0.x < x0) {
            x0 = v0.x
        }
        if (v1.x < x0) {
            x0 = v1.x
        }
        if (v0.x > x1) {
            x1 = v0.x
        }
        if (v1.x > x1) {
            x1 = v1.x
        }
        var y0 = java.lang.Float.POSITIVE_INFINITY
        var y1 = java.lang.Float.NEGATIVE_INFINITY
        if (v0.y < y0) {
            y0 = v0.y
        }
        if (v1.y < y0) {
            y0 = v1.y
        }
        if (v0.y > y1) {
            y1 = v0.y
        }
        if (v1.y > y1) {
            y1 = v1.y
        }
        var z0 = java.lang.Float.POSITIVE_INFINITY
        var z1 = java.lang.Float.NEGATIVE_INFINITY
        if (v0.z < z0) {
            z0 = v0.z
        }
        if (v1.z < z0) {
            z0 = v1.z
        }
        if (v0.z > z1) {
            z1 = v0.z
        }
        if (v1.z > z1) {
            z1 = v1.z
        }
        return BBox(Point3D(x0, y0, z0), Point3D(x1, y1, z1))
    }
}
