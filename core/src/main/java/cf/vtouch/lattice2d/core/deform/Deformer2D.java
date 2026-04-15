package cf.vtouch.lattice2d.core.deform;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

@FunctionalInterface
public interface Deformer2D {
    Mesh2D apply(Mesh2D input);

    static Deformer2D identity() {
        return input -> input;
    }
}
