package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

public record AffineTransform2D(
        float m00,
        float m01,
        float m02,
        float m10,
        float m11,
        float m12
) {
    private static final AffineTransform2D IDENTITY = new AffineTransform2D(1f, 0f, 0f, 0f, 1f, 0f);

    public static AffineTransform2D identity() {
        return IDENTITY;
    }

    public AffineTransform2D combine(AffineTransform2D local) {
        return new AffineTransform2D(
                m00 * local.m00 + m01 * local.m10,
                m00 * local.m01 + m01 * local.m11,
                m00 * local.m02 + m01 * local.m12 + m02,
                m10 * local.m00 + m11 * local.m10,
                m10 * local.m01 + m11 * local.m11,
                m10 * local.m02 + m11 * local.m12 + m12
        );
    }

    public Mesh2D apply(Mesh2D input) {
        return input.mapVertices((index, vertex) -> {
            float x = vertex.x();
            float y = vertex.y();
            float nx = m00 * x + m01 * y + m02;
            float ny = m10 * x + m11 * y + m12;
            return vertex.withPosition(nx, ny);
        });
    }
}
