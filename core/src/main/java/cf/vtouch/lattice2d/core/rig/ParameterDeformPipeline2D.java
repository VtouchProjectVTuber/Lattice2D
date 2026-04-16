package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ParameterDeformPipeline2D {
    private final List<ParameterDeformer2D> deformers = new ArrayList<>();

    public ParameterDeformPipeline2D add(ParameterDeformer2D deformer) {
        deformers.add(Objects.requireNonNull(deformer, "deformer must not be null"));
        return this;
    }

    public Mesh2D apply(Mesh2D source, ParameterState2D state) {
        Mesh2D result = source;
        for (ParameterDeformer2D deformer : deformers) {
            result = deformer.apply(result, state);
        }
        return result;
    }
}
