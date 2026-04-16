package cf.vtouch.lattice2d.core.scene;

public record ColorRgba2D(float r, float g, float b, float a) {
    public static final ColorRgba2D WHITE = new ColorRgba2D(1f, 1f, 1f, 1f);

    public ColorRgba2D {
        r = clamp01(r);
        g = clamp01(g);
        b = clamp01(b);
        a = clamp01(a);
    }

    private static float clamp01(float value) {
        return Math.max(0f, Math.min(1f, value));
    }
}
