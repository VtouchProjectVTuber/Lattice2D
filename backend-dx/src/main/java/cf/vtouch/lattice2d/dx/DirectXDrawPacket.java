package cf.vtouch.lattice2d.dx;

import cf.vtouch.lattice2d.core.clip.ClipMask2D;
import cf.vtouch.lattice2d.core.geom.Mesh2D;
import cf.vtouch.lattice2d.core.scene.RenderState2D;

public record DirectXDrawPacket(
        String textureRef,
        Mesh2D mesh,
        ClipMask2D clipMask,
        RenderState2D renderState,
        int layer,
        int zIndex
) {
}
