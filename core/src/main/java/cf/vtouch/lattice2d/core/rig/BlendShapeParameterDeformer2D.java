package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class BlendShapeParameterDeformer2D implements ParameterDeformer2D {
    private final Map<String, BlendShape2D> channels = new LinkedHashMap<>();

    public BlendShapeParameterDeformer2D channel(String parameterId, BlendShape2D blendShape) {
        channels.put(
                Objects.requireNonNull(parameterId, "parameterId must not be null"),
                Objects.requireNonNull(blendShape, "blendShape must not be null")
        );
        return this;
    }

    @Override
    public Mesh2D apply(Mesh2D input, ParameterState2D state) {
        Mesh2D result = input;
        for (Map.Entry<String, BlendShape2D> entry : channels.entrySet()) {
            float weight = state.value(entry.getKey());
            if (weight == 0f) {
                continue;
            }
            result = entry.getValue().apply(result, weight);
        }
        return result;
    }
}
