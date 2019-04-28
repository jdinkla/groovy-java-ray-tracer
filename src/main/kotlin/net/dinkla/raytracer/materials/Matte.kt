package net.dinkla.raytracer.materials

import net.dinkla.raytracer.colors.Color
import net.dinkla.raytracer.colors.ColorAccumulator
import net.dinkla.raytracer.hits.Shade
import net.dinkla.raytracer.brdf.Lambertian
import net.dinkla.raytracer.lights.AreaLight
import net.dinkla.raytracer.math.Ray
import net.dinkla.raytracer.math.Vector3D
import net.dinkla.raytracer.world.World
import java.util.*

open class Matte(val color: Color = Color.WHITE, val ka: Double = 0.25, val kd: Double = 0.75) : IMaterial {

    val ambientBRDF: Lambertian = Lambertian()
    val diffuseBRDF: Lambertian = Lambertian()

    init {
        setKa(ka)
        setKd(kd)
        setCd(color)
    }

    private fun setKa(ka: Double) {
        ambientBRDF.kd = ka
    }

    private fun setKd(kd: Double) {
        diffuseBRDF.kd = kd
    }

    private fun setCd(cd: Color) {
        ambientBRDF.cd = cd
        diffuseBRDF.cd = cd
    }

    override fun shade(world: World, sr: Shade): Color {
        val wo = -sr.ray.direction
        var L = getAmbientColor(world, sr, wo)
        for (light in world.lights) {
            val wi = light.getDirection(sr)
            val nDotWi = wi.dot(sr.normal)
            if (nDotWi > 0) {
                var inShadow = false
                if (light.shadows) {
                    val shadowRay = Ray(sr.hitPoint, wi)
                    inShadow = light.inShadow(world, shadowRay, sr)
                }
                if (!inShadow) {
                    val f = diffuseBRDF.f(sr, wo, wi)
                    val l = light.L(world, sr)
                    val flndotwi = f.times(l).times(nDotWi)
                    L = L.plus(flndotwi)
                }
            }
        }
        return L
    }

    /*
    	Vector3D 	wo 			= -sr.ray.direction;
	RGBColor 	L 			= ambient_brdf->rho(sr, wo) * sr.w.ambient_ptr->L(sr);
	int 		num_lights	= sr.w.lights.size();

	for (int j = 0; j < num_lights; j++) {
		Vector3D wi = sr.w.lights[j]->get_direction(sr);
		double ndotwi = sr.normal * wi;

		if (ndotwi > 0.0)
			L += diffuse_brdf->f(sr, wo, wi) * sr.w.lights[j]->L(sr) * ndotwi;
	}

	return (L);
    */
    override fun areaLightShade(world: World, sr: Shade): Color {
        val wo = -sr.ray.direction
        var L = getAmbientColor(world, sr, wo)
        val S = ColorAccumulator()
        for (light1 in world.lights) {
            if (light1 is AreaLight) {
                val ls = light1.getSamples(sr)
                for (sample in ls) {
                    val nDotWi = sample.wi!!.dot(sr.normal)
                    if (nDotWi > 0) {
                        var inShadow = false
                        if (light1.shadows) {
                            val shadowRay = Ray(sr.hitPoint, sample.wi!!)
                            inShadow = light1.inShadow(world, shadowRay, sr, sample)
                        }
                        if (!inShadow) {
                            val f = diffuseBRDF.f(sr, wo, sample.wi!!)
                            val l = light1.L(world, sr, sample)
                            val flndotwi = f.times(l).times(nDotWi)
                            // TODO: hier ist der Unterschied zu shade()
                            val f1 = light1.G(sr, sample) / light1.pdf(sr)
                            val T = flndotwi.times(f1)
                            S.plus(T)
                        }
                    }
                }
            }
        }
        L = L.plus(S.average)
        return L
    }

    protected fun getAmbientColor(world: World, sr: Shade, wo: Vector3D): Color {
        val c1 = ambientBRDF.rho(sr, wo)
        val c2 = world.ambientLight.L(world, sr)
        return c1.times(c2)
    }

    override fun getLe(sr: Shade): Color {
        return diffuseBRDF.rho(sr, null!!)
    }

    companion object {
        // TODO used?
        val materials = arrayOf(Matte(Color(0.0, 0.0, 1.0), 1.0, 1.0), Matte(Color(0.0, 1.0, 1.0), 1.0, 1.0), Matte(Color(1.0, 1.0, 0.0), 1.0, 1.0), Matte(Color(0.0, 1.0, 0.0), 1.0, 1.0), Matte(Color(1.0, 0.0, 0.0), 1.0, 1.0), Matte(Color(1.0, 0.0, 1.0), 1.0, 1.0), Matte(Color(1.0, 1.0, 1.0), 1.0, 1.0))
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is Matte) {
            return ambientBRDF.equals(other.ambientBRDF) && diffuseBRDF.equals(other.diffuseBRDF)
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(ambientBRDF, diffuseBRDF)

    override fun toString(): String = "Matte $ambientBRDF $diffuseBRDF"
}

