package cf.vtouch.lattice2d.core.geom;

public record UvTransform2D(
        float m00,
        float m01,
        float m02,
        float m10,
        float m11,
        float m12
) {
    private static final UvTransform2D IDENTITY = new UvTransform2D(1f, 0f, 0f, 0f, 1f, 0f);

    public static UvTransform2D identity() {
        return IDENTITY;
    }

    public static UvTransform2D translate(float du, float dv) {
        return new UvTransform2D(1f, 0f, du, 0f, 1f, dv);
    }

    public static UvTransform2D scale(float su, float sv) {
        return new UvTransform2D(su, 0f, 0f, 0f, sv, 0f);
    }

    public static UvTransform2D rotateRad(float radians) {
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);
        return new UvTransform2D(c, -s, 0f, s, c, 0f);
    }

    public static UvTransform2D rotateAroundRad(float pivotU, float pivotV, float radians) {
        return translate(pivotU, pivotV)
                .combine(rotateRad(radians))
                .combine(translate(-pivotU, -pivotV));
    }

    public static UvTransform2D fromRegion(UvRect2D region) {
        return translate(region.u0(), region.v0())
                .combine(scale(region.width(), region.height()));
    }

    public UvTransform2D combine(UvTransform2D local) {
        return new UvTransform2D(
                m00 * local.m00 + m01 * local.m10,
                m00 * local.m01 + m01 * local.m11,
                m00 * local.m02 + m01 * local.m12 + m02,
                m10 * local.m00 + m11 * local.m10,
                m10 * local.m01 + m11 * local.m11,
                m10 * local.m02 + m11 * local.m12 + m12
        );
    }

    public Vertex2D apply(Vertex2D input) {
        float u = input.u();
        float v = input.v();
        float nu = m00 * u + m01 * v + m02;
        float nv = m10 * u + m11 * v + m12;
        return input.withUv(nu, nv);
    }

    public Mesh2D apply(Mesh2D input) {
        return input.mapVertices((index, vertex) -> apply(vertex));
    }
}
