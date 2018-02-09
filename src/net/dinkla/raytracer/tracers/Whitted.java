package net.dinkla.raytracer.tracers;

import net.dinkla.raytracer.colors.Color;
import net.dinkla.raytracer.hits.Shade;
import net.dinkla.raytracer.math.MathUtils;
import net.dinkla.raytracer.math.Ray;
import net.dinkla.raytracer.math.WrappedFloat;
import net.dinkla.raytracer.utilities.Counter;
import net.dinkla.raytracer.worlds.World;
import org.apache.log4j.Logger;

public class Whitted extends Tracer {

    static final Logger LOGGER = Logger.getLogger(Whitted.class);

//    double f = 0.6;
//    Color fc = new RGBColor(0.9, 0.1, 1.0);
//    Color fc = new RGBColor(0.5, 0.5, 0.5);

    public Whitted(World world) {
        super(world);
    }

    @Override
    public Color trace(Ray ray) {
        Counter.count("Whitted.trace1");
        return trace(ray, 0);
    }

    @Override
    public Color trace(Ray ray, int depth) {
        Counter.count("Whitted.trace2");
        return trace(ray, WrappedFloat.Companion.createMax(), depth);
    }

    @Override
    public Color trace(Ray ray, WrappedFloat tmin, int depth) {
        //LOGGER.debug("trace " + ray + " at depth " + depth);
        Counter.count("Whitted.trace3");
        Color color = null;
        if (depth > world.getViewPlane().maxDepth) {
            color = Color.BLACK;
        } else {
            Shade sr = new Shade();
            boolean hit = world.hit(ray, sr);
            if (hit) {
                sr.depth = depth;
                sr.ray = ray;
                tmin.setValue(sr.t);
                if (null == sr.getMaterial()) {
                    LOGGER.error("Material is NULL for ray " + ray + " and sr " + sr);
                    color =  Color.errorColor;
                } else {
                    color =  sr.getMaterial().shade(world, sr);
                }
            } else {
                // No hit -> Background
                tmin.setValue(MathUtils.K_HUGEVALUE);
                color =  world.getBackgroundColor();
            }
        }
/*
        double ff =  Math.sqrt(tmin.getValue() * f);
        color =  color.plus(fc.mult(ff));
*/
        return color;
    }

}
