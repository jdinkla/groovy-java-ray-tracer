package net.dinkla.raytracer.lights

import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.hits.Shade
import net.dinkla.raytracer.materials.Material
import net.dinkla.raytracer.math.Ray
import net.dinkla.raytracer.math.Vector3D
import net.dinkla.raytracer.samplers.Sampler
import net.dinkla.raytracer.worlds.World

class EnvironmentLight : Light() {

    var sampler: Sampler? = null
    var material: Material? = null
    var u: Vector3D = Vector3D.ZERO
    var v: Vector3D = Vector3D.ZERO
    var w: Vector3D = Vector3D.ZERO
    var wi: Vector3D = Vector3D.ZERO

    override fun inShadow(world: World, ray: Ray, sr: Shade): Boolean {
        return world.inShadow(ray, sr, java.lang.Double.MAX_VALUE)
    }

    override fun getDirection(sr: Shade): Vector3D {
        w = Vector3D(sr.normal)
        v = Vector3D(0.0034, 1.0, 0.0071).cross(w)
        u = v.cross(w)
        val sp = sampler!!.sampleHemisphere()
        wi = u.times(sp.x).plus(v.times(sp.y)).plus(w.times(sp.z))
        return wi
    }

    override fun L(world: World, sr: Shade): Color {
        return material!!.getLe(sr)
    }
}
