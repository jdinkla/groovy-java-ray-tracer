package net.dinkla.raytracer.math

import spock.lang.Specification

/**
 * Created by jdinkla on 07.06.17.
 */
class AxisTest extends Specification {

    def "FromInt"() {
        expect: Axis.fromInt(0) == Axis.X
        and: Axis.fromInt(1) == Axis.Y
        and: Axis.fromInt(2) == Axis.Z
        and: Axis.fromInt(3) == null
    }

    def "Next"() {
        expect: Axis.X.next() == Axis.Y
        and: Axis.Y.next() == Axis.Z
        and: Axis.Z.next() == Axis.X
    }
}
