package cf.vtouch.lattice2d.core.rig;

import java.util.Objects;

public record ParameterSpec2D(
        String id,
        float min,
        float max,
        float defaultValue,
        ParameterClampMode2D clampMode,
        ParameterCurve2D curve
) {
    public ParameterSpec2D {
        id = Objects.requireNonNull(id, "id must not be null");
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        clampMode = Objects.requireNonNull(clampMode, "clampMode must not be null");
        curve = Objects.requireNonNull(curve, "curve must not be null");
    }

    public static ParameterSpec2D linear(String id, float min, float max, float defaultValue) {
        return new ParameterSpec2D(id, min, max, defaultValue, ParameterClampMode2D.CLAMP, ParameterCurve2D.LINEAR);
    }

    public float resolve(ParameterState2D state) {
        float raw = state.value(id, defaultValue);
        float sanitized = sanitize(raw);
        float normalized = normalize(sanitized);
        float curved = curve.apply(normalized);
        return denormalize(curved);
    }

    public float sanitize(float rawValue) {
        return switch (clampMode) {
            case NONE -> rawValue;
            case CLAMP -> Math.max(min, Math.min(max, rawValue));
            case WRAP -> wrap(rawValue);
        };
    }

    public float normalize(float value) {
        if (max == min) {
            return 0f;
        }
        return (value - min) / (max - min);
    }

    public float denormalize(float normalized) {
        return min + normalized * (max - min);
    }

    private float wrap(float value) {
        float range = max - min;
        if (range == 0f) {
            return min;
        }
        float wrapped = (value - min) % range;
        if (wrapped < 0f) {
            wrapped += range;
        }
        return wrapped + min;
    }
}
