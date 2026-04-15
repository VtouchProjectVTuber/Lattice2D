package cf.vtouch.lattice2d.core.scene;

import cf.vtouch.lattice2d.core.clip.ClipMask2D;
import cf.vtouch.lattice2d.core.geom.Mesh2D;

public record DrawCommand2D(
        String textureRef,
        Mesh2D mesh,
        ClipMask2D clipMask,
        DrawOrder2D order
) {
}
