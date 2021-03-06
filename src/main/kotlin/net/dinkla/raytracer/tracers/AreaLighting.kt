package net.dinkla.raytracer.tracers

import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.hits.Shade
import net.dinkla.raytracer.math.Ray
import net.dinkla.raytracer.interfaces.jvm.getLogger
import net.dinkla.raytracer.world.World

class AreaLighting(var world: World) : Tracer {

    override fun trace(ray: Ray, depth: Int): Color {
        //LOGGER.debug("trace $ray at depth $depth")
        if (depth > world.viewPlane.maxDepth) {
            return Color.BLACK
        } else {
            val sr = Shade()
            if (world.hit(ray, sr)) {
                sr.depth = depth
                sr.ray = ray
                assert(null != sr.material)
                return sr.material?.areaLightShade(world, sr) ?: world.backgroundColor
            } else {
                return world.backgroundColor
            }
        }
    }

    companion object {
        internal val LOGGER = getLogger(this::class.java)
    }

}