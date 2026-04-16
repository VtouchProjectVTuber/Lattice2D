package cf.vtouch.lattice2d.core.rig;

@FunctionalInterface
public interface ParameterCurve2D {
    ParameterCurve2D LINEAR = t -> t;
    ParameterCurve2D SMOOTHSTEP = t -> t * t * (3f - 2f * t);
    ParameterCurve2D EASE_IN_OUT_CUBIC = t -> t < 0.5f
            ? 4f * t * t * t
            : 1f - (float) Math.pow(-2f * t + 2f, 3f) / 2f;

    float apply(float normalized);
}
