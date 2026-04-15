package cf.vtouch.lattice2d.core.deform;

import cf.vtouch.lattice2d.core.geom.Mesh2D;
import cf.vtouch.lattice2d.core.geom.Vertex2D;

public final class AffineDeformer2D implements Deformer2D {
    private final float m00;
    private final float m01;
    private final float m02;
    private final float m10;
    private final float m11;
    private final float m12;

    private AffineDeformer2D(float m00, float m01, float m02, float m10, float m11, float m12) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
    }

    public static AffineDeformer2D identity() {
        return new AffineDeformer2D(1f, 0f, 0f, 0f, 1f, 0f);
    }

    public static AffineDeformer2D translate(float tx, float ty) {
        return new AffineDeformer2D(1f, 0f, tx, 0f, 1f, ty);
    }

    public static AffineDeformer2D scale(float sx, float sy) {
        return new AffineDeformer2D(sx, 0f, 0f, 0f, sy, 0f);
    }

    public static AffineDeformer2D rotateRad(float radians) {
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);
        return new AffineDeformer2D(c, -s, 0f, s, c, 0f);
    }

    @Override
    public Mesh2D apply(Mesh2D input) {
        return input.mapVertices(this::transformVertex);
    }

    private Vertex2D transformVertex(int index, Vertex2D original) {
        float x = original.x();
        float y = original.y();
        float nx = m00 * x + m01 * y + m02;
        float ny = m10 * x + m11 * y + m12;
        return original.withPosition(nx, ny);
    }
}
