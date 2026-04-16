package cf.vtouch.lattice2d.dx;

import cf.vtouch.lattice2d.core.scene.DrawCommand2D;

public final class DirectXCommandEncoder {
    public DirectXDrawPacket encode(DrawCommand2D command) {
        return new DirectXDrawPacket(
                command.textureRef(),
                command.mesh(),
                command.clipMask(),
                command.renderState(),
                command.order().layer(),
                command.order().zIndex()
        );
    }
}
