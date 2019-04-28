package net.dinkla.raytracer.world

import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.lights.AmbientOccluder
import net.dinkla.raytracer.lights.PointLight
import net.dinkla.raytracer.materials.Matte
import net.dinkla.raytracer.math.Point3D
import net.dinkla.raytracer.math.Vector3D
import net.dinkla.raytracer.objects.Sphere
import net.dinkla.raytracer.tracers.AreaLighting
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.function.BiPredicate

class BuilderTest {

    @Test
    fun `should store name of world`() {
        val id = "idOfWorld"
        val world = Builder.build(id) {}
        assertEquals(id, world.id)
    }

    @Test
    fun `should set camera`() {
        val d = 500.0
        val eye = Point3D(0, 100, 200)
        val lookAt = Point3D(1, 2, 3)
        val world = Builder.build("id") {
            camera(d = d, eye = p(0, 100, 200), lookAt = p(1, 2, 3), up = Vector3D.JITTER)
        }
        assertNotNull(world.camera)
        assertEquals(eye, world.camera?.eye)
        assertEquals(lookAt, world.camera?.lookAt)
        assertEquals(Vector3D.JITTER, world.camera?.up)
    }

    @Test
    fun `should set ambient light`() {
        val ls = 123.45
        val color = Color.BLUE
        val world = Builder.build("id") {
            ambientLight(color = color, ls = ls)
        }
        assertNotNull(world.ambientLight)
        assertEquals(color, world.ambientLight.color)
        assertEquals(ls, world.ambientLight.ls)
    }

    @Test
    fun `should add point light`() {
        val ls = 0.98
        val color = Color.BLUE
        val location = Point3D(0, 100, 200)
        val world = Builder.build("id") {
            lights {
                pointLight(location = location, ls = ls, color = color)
            }
        }
        assertNotNull(world.lights)
        assertEquals(1, world.lights.size)
        val light = world.lights[0]
        assertTrue(light is PointLight)
        if (light is PointLight) {
            assertEquals(color, light.color)
            assertEquals(location, light.location)
            assertEquals(ls, light.ls)
        }
    }

    @Test
    fun `should store matte materials`() {
        val id = "m1"
        val cd = Color(1.0, 0.5, 0.3)
        val world = Builder.build("id") {
            materials {
                matte(id = id, cd = cd)
            }
        }
        assertEquals(1, world.materials.size)
        assertTrue(world.materials.containsKey(id))
        assertEquals(Matte(cd), world.materials[id])
    }

    @Test
    fun `should store sphere objects using materials`() {
        val id = "m1"
        val cd = Color(1.0, 0.5, 0.3)
        val center = Point3D(0, 100, 200)
        val radius = 80.0
        val world = Builder.build("id") {
            materials {
                matte(id = id, cd = cd)
            }
            objects {
                sphere(material = id, center = center, radius = radius)
            }
        }
        assertEquals(1, world.materials.size)
        assertTrue(world.materials.containsKey(id))
        assertEquals(Matte(cd), world.materials[id])

        assertEquals(1, world.objects.size)
        val sphere = world.objects[0]
        assertTrue(sphere is Sphere)
        if (sphere is Sphere) {
            assertEquals(center, sphere.center)
            assertEquals(radius, sphere.radius)
        }

        assertEquals(1, world.compound.size())
    }

    @Disabled
    @Test
    fun testCreate1() {
        val f = findExample("World20.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 3)
        assertEquals(w.lights.size, 1)
    }

    @Disabled
    @Test
    fun testCreate2() {
        val f = findExample("World7.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 6)
        assertEquals(w.lights.size, 3)
    }

    @Disabled
    @Test
    fun testCreate3() {
        val f = findExample("World14.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 2)
        assertEquals(w.lights.size, 0)
        assertEquals(AmbientOccluder::class.java, w.ambientLight.javaClass)
    }

    @Disabled
    @Test
    fun testCreate4() {
        val f = findExample("World17.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 10)
        assertEquals(w.lights.size, 2)
    }

    @Disabled
    @Test
    fun testCreate5() {
        val f = findExample("World23.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 26)
        assertEquals(w.lights.size, 1)
        assertEquals(w.tracer.javaClass, AreaLighting::class.java)
    }

    @Disabled
    @Test
    fun testCreate6() {
        val f = findExample("World26.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 2)
        assertEquals(w.lights.size, 1)
    }

    @Disabled
    @Test
    fun testCreate7() {
        val f = findExample("World34.groovy").get().toFile()
        val w = Builder.create(f)
        assertNotNull(w.viewPlane, "viewPlane == null")
        assertNotNull(w.camera, "camera == null")
        assertNotNull(w.tracer, "tracer == null")
        assertEquals(w.size(), 5)
        assertEquals(w.lights.size, 1)
    }

    @Disabled
    @Test
    fun testCreate8() {
        val f = findExample("World38.groovy").get().toFile()
        val w = Builder.create(f)
        assertEquals(w.size(), 6)
    }

    companion object {

        private val DIR = "examples"

        private fun findExample(filename: String): Optional<Path> {
            val pr = BiPredicate<Path?, BasicFileAttributes> { p, a ->
                val fn = p?.getFileName().toString()
                filename == fn
            }
            try {
                val f = File(DIR)
                val ps = Files.find(f.toPath(), 99, pr)
                return ps.findFirst()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return Optional.empty()
        }
    }

}