package net.dinkla.raytracer.cameras.lenses;

import net.dinkla.raytracer.ViewPlane;
import net.dinkla.raytracer.math.*;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 12.04.2010
 * Time: 12:20:46
 * To change this template use File | Settings | File Templates.
 */
public class Pinhole extends AbstractLens {

    static final Logger LOGGER = Logger.getLogger(Pinhole.class);

    public float d;
    // TODO zoom camera
//    public float zoom;

    public Pinhole(ViewPlane viewPlane) {
        super(viewPlane);            
        this.d = 1.0f;
//        this.zoom = zoom;
        //viewPlane.size /= zoom;
    }

    public Ray getRaySingle(int r, int c) {
        float x = (float) (viewPlane.size * (c - 0.5 * viewPlane.resolution.hres()));
        float y = (float) (viewPlane.size * (r - 0.5 * viewPlane.resolution.vres()));
        Ray ray = new Ray(eye, getRayDirection(x, y));
        return ray;
    }
    
    public Ray getRaySampled(int r, int c, Point2DF sp) {
        float x = (float) (viewPlane.size * (c - 0.5 * viewPlane.resolution.hres() + sp.x()));
        float y = (float) (viewPlane.size * (r - 0.5 * viewPlane.resolution.vres() + sp.y()));
        Ray ray = new Ray(eye, getRayDirection(x, y));
        return ray;
    }

    protected Vector3DF getRayDirection(float x, float y) {
        // xu + yv - dw
//        Vector3DF dir = u.mult(x).plus(v.mult(y)).minus(w.mult(d));
        Vector3DF dir = new Vector3DF(uvw.pm(x, y, d));
        return dir.normalize();
    }

}
