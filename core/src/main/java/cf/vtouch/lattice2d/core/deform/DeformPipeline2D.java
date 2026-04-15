package cf.vtouch.lattice2d.core.deform;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

import java.util.ArrayList;
import java.util.List;

public final class DeformPipeline2D {
    private final List<Deformer2D> deformers = new ArrayList<>();

    public DeformPipeline2D add(Deformer2D deformer) {
        deformers.add(deformer);
        return this;
    }

    public Mesh2D apply(Mesh2D source) {
        Mesh2D result = source;
        for (Deformer2D deformer : deformers) {
            result = deformer.apply(result);
        }
        return result;
    }
}
