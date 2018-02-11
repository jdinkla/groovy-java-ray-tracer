package net.dinkla.raytracer.objects.acceleration.kdtree

import net.dinkla.raytracer.math.BBox
import net.dinkla.raytracer.math.Point3D
import net.dinkla.raytracer.objects.GeometricObject
import net.dinkla.raytracer.math.Axis
import net.dinkla.raytracer.utilities.Counter
import org.apache.log4j.Logger

import java.util.ArrayList

class SpatialMedianBuilder : IKDTreeBuilder {

    override var maxDepth = 15
    var minChildren = 4

    override fun build(tree: KDTree, voxel: BBox): AbstractNode {
        return build(tree.objects, tree.boundingBox, 0)
    }

    fun build(objects: List<GeometricObject>, voxel: BBox?, depth: Int): AbstractNode {

        Counter.count("KDtree.build")

        var node: AbstractNode? = null //new Leaf(objects);
        var voxelL: BBox? = null
        var voxelR: BBox? = null

        if (objects.size < minChildren || depth >= maxDepth) {
            Counter.count("KDtree.build.leaf")
            node = Leaf(objects)
            return node
        }

        Counter.count("KDtree.build.node")

        val half = voxel!!.q!!.minus(voxel.p!!).times(0.5)
        val mid = voxel.p.plus(half)

        var split: Double? = null

        val objectsL = ArrayList<GeometricObject>()
        val objectsR = ArrayList<GeometricObject>()

        if (depth % 3 == 0) {
            // x
            split = mid.x

            val q1 = Point3D(mid.x, voxel.q!!.y, voxel.q.z)
            voxelL = BBox(voxel.p, q1)

            val p2 = Point3D(mid.x, voxel.p.y, voxel.p.z)
            voxelR = BBox(p2, voxel.q)

            for (`object` in objects) {
                val bbox = `object`.boundingBox
                if (bbox.p!!.x <= split) {
                    objectsL.add(`object`)
                }
                if (bbox.q!!.x >= split) {
                    objectsR.add(`object`)
                }
            }

        } else if (depth % 3 == 1) {
            // y
            split = mid.y

            val q1 = Point3D(voxel.q!!.x, mid.y, voxel.q.z)
            voxelL = BBox(voxel.p, q1)

            val p2 = Point3D(voxel.p.x, mid.y, voxel.p.z)
            voxelR = BBox(p2, voxel.q)

            for (`object` in objects) {
                val bbox = `object`.boundingBox
                if (bbox.p!!.y <= split) {
                    objectsL.add(`object`)
                }
                if (bbox.q!!.y >= split) {
                    objectsR.add(`object`)
                }
            }
        } else if (depth % 3 == 2) {
            // z
            split = mid.z

            val q1 = Point3D(voxel.q!!.x, voxel.q.y, mid.z)
            voxelL = BBox(voxel.p, q1)

            val p2 = Point3D(voxel.p.x, voxel.p.y, mid.z)
            voxelR = BBox(p2, voxel.q)

            for (`object` in objects) {
                val bbox = `object`.boundingBox
                if (bbox.p!!.z <= split) {
                    objectsL.add(`object`)
                }
                if (bbox.q!!.z >= split) {
                    objectsR.add(`object`)
                }
            }
        }

        LOGGER.info("Splitting " + objects.size + " objects into " + objectsL.size + " and " + objectsR.size + " objects at " + split + " with depth " + depth)
        val left = build(objectsL, voxelL, depth + 1)
        val right = build(objectsR, voxelR, depth + 1)

        node = InnerNode(left, right, voxel, split!!, Axis.fromInt(depth % 3))

        return node
    }

    companion object {

        internal val LOGGER = Logger.getLogger(SpatialMedianBuilder::class.java)
    }
}
