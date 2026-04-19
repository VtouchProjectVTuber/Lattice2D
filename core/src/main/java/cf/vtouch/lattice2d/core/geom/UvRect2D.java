package cf.vtouch.lattice2d.core.geom;

public record UvRect2D(float u0, float v0, float u1, float v1) {
    public UvRect2D {
        if (u1 < u0) {
            throw new IllegalArgumentException("u1 must be >= u0");
        }
        if (v1 < v0) {
            throw new IllegalArgumentException("v1 must be >= v0");
        }
    }

    public float width() {
        return u1 - u0;
    }

    public float height() {
        return v1 - v0;
    }
}
