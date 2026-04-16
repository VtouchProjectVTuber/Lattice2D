package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

@FunctionalInterface
public interface ParameterDeformer2D {
    Mesh2D apply(Mesh2D input, ParameterState2D state);

    static ParameterDeformer2D identity() {
        return (input, state) -> input;
    }
}
