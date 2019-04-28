package net.dinkla.raytracer.tracers

import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.math.Ray
import net.dinkla.raytracer.math.WrappedFloat
import net.dinkla.raytracer.world.World
import org.slf4j.LoggerFactory

abstract class Tracer(var world: World) {

    // TODO: Warum so viele trace-Funktionen?
    // TODO: Sollte nicht die mit den meisten Parametern abstract sein?
    abstract fun trace(ray: Ray, depth: Int): Color

    open fun trace(ray: Ray, tmin: WrappedFloat, depth: Int): Color {
        //LOGGER.debug("trace " + ray + " tmin=" + tmin + " at depth " + depth);
        return trace(ray, depth)
    }

    open fun trace(ray: Ray): Color {
        //LOGGER.debug("trace " + ray);
        return trace(ray, 0)
    }

    companion object {
        internal val LOGGER = LoggerFactory.getLogger(this::class.java)
    }
}
