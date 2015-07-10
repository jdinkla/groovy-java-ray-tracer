package net.dinkla.raytracer.objects;

import net.dinkla.raytracer.hits.Hit;
import net.dinkla.raytracer.math.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 10.04.2010
 * Time: 21:12:03
 * To change this template use File | Settings | File Templates.
 */
public class PlaneTest {

    // Plane
    Point3DF p;
    Normal normal;
    Plane plane;

    // Ray
    Point3DF o;
    Vector3DF d;
    Ray ray;
    
    // Sample
    Hit sr;

    @BeforeMethod
    public void init() {
        sr = new Hit();
    }

    @Test
    public void construct() {
        new Plane(null, null);
    }

    // q=0 plane, point below, vector up, hit
    @Test
    public void hit0() {
        p = new Point3DF(0, 0, 0);
        normal = new Normal(0, 1, 0);
        plane = new Plane(p, normal);

        o = new Point3DF(-1,-1,-1);
        d = new Vector3DF(0,1,0);
        ray = new Ray(o, d);

        boolean isHit = plane.hit(ray, sr);
        assert isHit;
        assertEquals(sr.getT(), 1.0f);
        assertEquals(sr.getNormal(), normal);
    }

    // q=0 plane, point above, vector up, no hit
    @Test
    public void hit1() {
        p = new Point3DF(0, 0, 0);
        normal = new Normal(0, 1, 0);
        plane = new Plane(p, normal);

        // point above, vector up
        o = new Point3DF(-1,1,-1);
        d = new Vector3DF(0,1,0);
        ray = new Ray(o, d);

        boolean isHit = plane.hit(ray, sr);
        assert !isHit;
        assertNull(sr.getNormal());
    }

    // q=0 plane upside down, point below, vector up, hit
    @Test
    public void hit2() {
        p = new Point3DF(0, 0, 0);
        normal = new Normal(0, -1, 0);
        plane = new Plane(p, normal);

        o = new Point3DF(-1, -1, -1);
        d = new Vector3DF(0, 1, 0);
        ray = new Ray(o, d);

        boolean isHit = plane.hit(ray, sr);
        assert isHit;
        assertEquals(sr.getT(), 1.0f);
        assertEquals(sr.getNormal(), normal);
    }

    // q=0 plane, point above, vector down, hit
    @Test
    public void hit3() {
        p = new Point3DF(0, 0, 0);
        normal = new Normal(0, 1, 0);
        plane = new Plane(p, normal);

        o = new Point3DF(1, 2, 1);
        d = new Vector3DF(0, -1, 0);
        ray = new Ray(o, d);

        boolean isHit = plane.hit(ray, sr);
        assert isHit;
        assertEquals(sr.getT(), 2.0f);
        assertEquals(sr.getNormal(), normal);
    }

    @Test
    public void hit4() {
        p = new Point3DF(0, 0, 0);
        normal = new Normal(1, 1, 0);
        plane = new Plane(p, normal);

        o = new Point3DF(-2, -2, 0);
        d = new Vector3DF(1, 1, 0);
        ray = new Ray(o, d);

        boolean isHit = plane.hit(ray, sr);
        assert isHit;
        assertEquals(sr.getT(), 2.0f);
        assertEquals(sr.getNormal(), normal);
    }

    @Test
    public void hit5() {
        p = new Point3DF(0.1234f, 0, 0);
        normal = new Normal(1, 0, 0);
        plane = new Plane(p, normal);

        o = new Point3DF(0, 4, 3);
        d = new Vector3DF(-1, 0, 0);
        ray = new Ray(o, d);

        boolean isHit = plane.hit(ray, sr);
        assert !isHit;
    }

}
