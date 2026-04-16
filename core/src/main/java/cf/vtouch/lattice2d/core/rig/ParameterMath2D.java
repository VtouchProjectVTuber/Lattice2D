package cf.vtouch.lattice2d.core.rig;

public final class ParameterMath2D {
    private ParameterMath2D() {
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float smoothStep(float edge0, float edge1, float x) {
        if (edge0 == edge1) {
            return x < edge0 ? 0f : 1f;
        }
        float t = (x - edge0) / (edge1 - edge0);
        t = Math.max(0f, Math.min(1f, t));
        return t * t * (3f - 2f * t);
    }

    public static SpringResult2D spring(float current, float target, float velocity, float stiffness, float damping, float deltaTime) {
        if (deltaTime <= 0f) {
            return new SpringResult2D(current, velocity);
        }
        float k = Math.max(0f, stiffness);
        float d = Math.max(0f, damping);

        float force = (target - current) * k - velocity * d;
        float nextVelocity = velocity + force * deltaTime;
        float nextValue = current + nextVelocity * deltaTime;
        return new SpringResult2D(nextValue, nextVelocity);
    }

    public record SpringResult2D(float value, float velocity) {
    }
}
