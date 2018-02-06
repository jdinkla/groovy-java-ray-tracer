package net.dinkla.raytracer.objects;

import net.dinkla.raytracer.hits.Hit;
import net.dinkla.raytracer.hits.ShadowHit;
import net.dinkla.raytracer.math.*;

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 21.04.2010
 * Time: 22:26:11
 * To change this template use File | Settings | File Templates.
 *
 * TODO: double vs. float
 *
 */
public class Torus extends GeometricObject {

    public final float a;
    public final float b;
    public final BBox bbox;

    public Torus(final float a, final float b) {
        this.a = a;
        this.b = b;
        bbox = new BBox(new Point3D(-a - b, -b, -a - b), new Point3D(a + b, b, a + b));
    }

    @Override
    public boolean hit(final Ray ray, Hit sr) {
        if (!bbox.hit(ray)) {
            return false;
        }
        final double x1 = ray.getO().getX();
        final double y1 = ray.getO().getY();
        final double z1 = ray.getO().getZ();

        final double d1 = ray.getD().getX();
        final double d2 = ray.getD().getY();
        final double d3 = ray.getD().getZ();

        double coeffs[] = new double[5];
        double roots[] = new double[4];

        double sumDSqrd = d1 * d1 + d2 * d2 + d3 * d3;
        double e = x1 * x1 + y1 * y1 + z1 * z1 - a * a - b * b;
        double f = x1 * d1 + y1 * d2 + z1 * d3;
        double fourASqrd = 4.0 * a * a;

        coeffs[0] = e * e - fourASqrd * (b * b - y1 * y1);
        coeffs[1] = 4.0 * f * e + 2.0 * fourASqrd * y1 * d2;
        coeffs[2] = 2.0 * sumDSqrd * e + 4.0 * f * f + fourASqrd * d2 * d2;
        coeffs[3] = 4.0 * sumDSqrd * f;
        coeffs[4] = sumDSqrd * sumDSqrd;

        int numRealRoots = Polynomials.INSTANCE.solveQuartic(coeffs, roots);
        boolean intersected = false;
        double t = Double.MAX_VALUE;

        if (numRealRoots == 0) {
            return false;
        }

        for (int j = 0; j < numRealRoots; j++) {
            if (roots[j] > MathUtils.K_EPSILON) {
                intersected = true;
                if (roots[j] < t) {
                    t = roots[j];
                }
            }
        }
        if (!intersected) {
            return false;
        }
        sr.setT((float) t);
        sr.setNormal(computeNormal(ray.linear((float) t)));
        return true;
    }

    public boolean hitF(final Ray ray, Hit sr) {
        if (!bbox.hit(ray)) {
            return false;
        }
        final float x1 = ray.getO().getX();
        final float y1 = ray.getO().getY();
        final float z1 = ray.getO().getZ();

        final float d1 = ray.getD().getX();
        final float d2 = ray.getD().getY();
        final float d3 = ray.getD().getZ();

        float coeffs[] = new float[5];
        float roots[] = new float[4];

        float sumDSqrd = d1 * d1 + d2 * d2 + d3 * d3;
        float e = x1 * x1 + y1 * y1 + z1 * z1 - a * a - b * b;
        float f = x1 * d1 + y1 * d2 + z1 * d3;
        float fourASqrd = 4.0f * a * a;

        coeffs[0] = e * e - fourASqrd * (b * b - y1 * y1);
        coeffs[1] = 4.0f * f * e + 2.0f * fourASqrd * y1 * d2;
        coeffs[2] = 2.0f * sumDSqrd * e + 4.0f * f * f + fourASqrd * d2 * d2;
        coeffs[3] = 4.0f * sumDSqrd * f;
        coeffs[4] = sumDSqrd * sumDSqrd;

        int numRealRoots = Polynomials.INSTANCE.solveQuartic(coeffs, roots);
        boolean intersected = false;
        float t = Float.MAX_VALUE;

        if (numRealRoots == 0) {
            return false;
        }

        for (int j = 0; j < numRealRoots; j++) {
            if (roots[j] > MathUtils.K_EPSILON) {
                intersected = true;
                if (roots[j] < t) {
                    t = roots[j];
                }
            }
        }
        if (!intersected) {
            return false;
        }
        sr.setT(t);
        sr.setNormal(computeNormal(ray.linear(t)));
        return true;
    }

    @Override
    public boolean shadowHit(final Ray ray, ShadowHit tmin) {
        return false;
    }

    @Override
    public BBox getBoundingBox() {
        return bbox;
    }

    private Normal computeNormal(Point3D p) {
        final double paramSquared = a * a + b * b;
        final double sumSquared = p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ();
        final double diff = sumSquared - paramSquared;
        final double x = 4.0 * p.getX() * diff;
        final double y = 4.0 * p.getY() * (diff + 2.0 * a * a);
        final double z = 4.0 * p.getZ() * diff;
        final Normal normal = new Normal((float)x, (float)y, (float)z).normalize();
        return normal;
    }
    
    /*
    private Normal computeNormal(Point3D p) {
        final float paramSquared = a * a + b * b;
        final float sumSquared = p.x * p.x + p.y * p.y + p.z * p.z;
        final float x = 4.0f * p.x * (sumSquared - paramSquared);
        final float y = 4.0f * p.y * (sumSquared - paramSquared + 2.0f * a * a);
        final float z = 4.0f * p.z * (sumSquared - paramSquared);
        final Normal normal = new Normal(x, y, z).normalize();
        return normal;
    }
*/
}
