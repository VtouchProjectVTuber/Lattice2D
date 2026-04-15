package cf.vtouch.lattice2d.dx;

import cf.vtouch.lattice2d.core.clip.ClipMask2D;
import cf.vtouch.lattice2d.core.geom.Mesh2D;

public record DirectXDrawPacket(
        String textureRef,
        Mesh2D mesh,
        ClipMask2D clipMask,
        int layer,
        int zIndex
) {
}
