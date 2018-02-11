package net.dinkla.raytracer.lights

import net.dinkla.raytracer.hits.Shade
import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.math.Ray
import net.dinkla.raytracer.math.Vector3D
import net.dinkla.raytracer.worlds.World

/**
 * TODO: DirectionalLight Light implementieren
 */
class DirectionalLight : Light() {

    var ls: Double = 0.toDouble()
    var color: Color
    var negatedDirection: Vector3D

    init {
        ls = 1.0
        color = Color.WHITE
        negatedDirection = Vector3D.DOWN.negate()
    }

    override fun L(world: World, sr: Shade): Color {
        return color.times(ls)
    }

    override fun getDirection(sr: Shade): Vector3D {
        return negatedDirection
    }

    override fun inShadow(world: World, ray: Ray, sr: Shade): Boolean {
        return world.inShadow(ray, sr, java.lang.Double.MAX_VALUE)
    }

    fun setDirection(direction: Vector3D) {
        this.negatedDirection = direction.negate()
    }

}
