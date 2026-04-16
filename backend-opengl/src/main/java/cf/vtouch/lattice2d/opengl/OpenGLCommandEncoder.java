package cf.vtouch.lattice2d.opengl;

import cf.vtouch.lattice2d.core.scene.DrawCommand2D;

public final class OpenGLCommandEncoder {
    public OpenGLDrawPacket encode(DrawCommand2D command) {
        return new OpenGLDrawPacket(
                command.textureRef(),
                command.mesh(),
                command.clipMask(),
                command.renderState(),
                command.order().layer(),
                command.order().zIndex()
        );
    }
}
