package net.dinkla.raytracer.math;

/**
 * Created by IntelliJ IDEA.
 * User: jorndinkla
 * Date: 10.04.2010
 * Time: 23:22:46
 * To change this template use File | Settings | File Templates.
 */
public class WrappedFloat implements Comparable<WrappedFloat>{

    public Double value;
    //public Double value;

    public WrappedFloat() {
        this.value = null;
    }

    public WrappedFloat(final double value) {
        this.value = value;
//        this.value =  value;
    }

    public void setMaxValue() {
        value = Double.MAX_VALUE;
        //value = Double.MAX_VALUE;
    }

    public Double getValue() {
        return value;
    }
    
    public void setValue(final double value) {
//        this.value =  value;
        this.value = value;
    }
    
    /**
     * null is treated as the smallest element
     *
     * null null    0
     * null Y       -1
     * X    null    1
     * X    Y       X.compareTo(Y)
     *
     * @param o
     * @return
     */
    public int compareTo(final WrappedFloat o) {
        if (null == value) {
            if (null == o.value) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (null == o.value) {
                return 1;
            } else {
                return value.compareTo(o.value);
            }
        }
    }

    @Override
    public String toString() {
        return null != value ? value.toString() : "null";
    }

    public boolean isLessThan(final WrappedFloat tmin) {
        return value.compareTo(tmin.value) == -1;
    }

    public static WrappedFloat createMax() {
        WrappedFloat f = new WrappedFloat();
        f.setMaxValue();
        return f;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WrappedFloat) {
            WrappedFloat wf = (WrappedFloat) obj;
            Double f = wf.getValue();
            if (value == null) {
                return f == null;
            } else {
                return this.value.equals(f);
            }
        } else {
            return false;
        }
    }
}
