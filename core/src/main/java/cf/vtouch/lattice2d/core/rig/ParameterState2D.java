package cf.vtouch.lattice2d.core.rig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ParameterState2D {
    private static final ParameterState2D EMPTY = new ParameterState2D(Map.of());

    private final Map<String, Float> values;

    private ParameterState2D(Map<String, Float> values) {
        this.values = Map.copyOf(values);
    }

    public static ParameterState2D empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public float value(String parameterId) {
        return value(parameterId, 0f);
    }

    public float value(String parameterId, float defaultValue) {
        return values.getOrDefault(parameterId, defaultValue);
    }

    public float value(ParameterSpec2D spec) {
        return spec.resolve(this);
    }

    public Map<String, Float> values() {
        return values;
    }

    public ParameterState2D with(String parameterId, float parameterValue) {
        Objects.requireNonNull(parameterId, "parameterId must not be null");
        Map<String, Float> copy = new LinkedHashMap<>(values);
        copy.put(parameterId, parameterValue);
        return new ParameterState2D(copy);
    }

    public ParameterState2D with(ParameterSpec2D spec, float rawValue) {
        Objects.requireNonNull(spec, "spec must not be null");
        return with(spec.id(), spec.sanitize(rawValue));
    }

    public static final class Builder {
        private final Map<String, Float> values = new LinkedHashMap<>();

        public Builder set(String parameterId, float parameterValue) {
            Objects.requireNonNull(parameterId, "parameterId must not be null");
            values.put(parameterId, parameterValue);
            return this;
        }

        public ParameterState2D build() {
            return values.isEmpty() ? ParameterState2D.empty() : new ParameterState2D(values);
        }
    }
}
