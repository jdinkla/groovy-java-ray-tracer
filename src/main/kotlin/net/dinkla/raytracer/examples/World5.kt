package net.dinkla.raytracer.examples

import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.world.Builder
import net.dinkla.raytracer.math.Normal
import net.dinkla.raytracer.math.Point3D
import net.dinkla.raytracer.world.WorldDef

object World5 : WorldDef {

    override fun world() = Builder.build("World5") {

        camera(d = 500.0, eye = p(0, 100, 200), lookAt = p(0, 0, 0))

        ambientLight(color = Color.WHITE, ls = 0.75)

        lights {
            pointLight(location = p(0, 100, 0), ls = 2.0)
        }

        materials {
            matte(id = "m1", cd = c(1.0, 0.0, 0.0))
            matte(id = "m2", cd = c(1.0, 1.0, 0.0))
            matte(id = "m3", cd = c(0.0, 0.3, 0.0))
        }

        objects {
            sphere(material = "m1", center = p(0, -25, 0), radius = 80.0)
            sphere(material = "m2", center = p(0, 30, 0), radius = 60.0)
            plane(material = "m3", point = Point3D.ORIGIN, normal = Normal(0, 1, 1))
        }
    }

}

