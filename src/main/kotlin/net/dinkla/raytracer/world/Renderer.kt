package net.dinkla.raytracer.world

import net.dinkla.raytracer.cameras.render.IRenderer
import net.dinkla.raytracer.films.Film
import net.dinkla.raytracer.objects.acceleration.kdtree.InnerNode
import net.dinkla.raytracer.interfaces.Counter
import net.dinkla.raytracer.interfaces.Timer
import net.dinkla.raytracer.interfaces.jvm.getLogger

class Renderer {

    var renderer: IRenderer? = null

    fun render(film: Film) {
        assert(null != renderer)

        val timer = Timer()
        timer.start()
        renderer?.render(film)
        timer.stop()
        LOGGER.info("rendering took " + timer.duration + " ms")

        // stats
        Counter.stats(30)

        println("Hits")
        InnerNode.hits.println()

        println("fails")
        InnerNode.fails.println()

        println("took " + timer.duration + " [ms]")

        Counter.reset()
    }

    companion object {
        internal val LOGGER = getLogger(this::class.java)
    }
}