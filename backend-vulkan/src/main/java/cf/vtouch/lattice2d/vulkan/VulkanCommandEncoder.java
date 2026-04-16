package cf.vtouch.lattice2d.vulkan;

import cf.vtouch.lattice2d.core.scene.DrawCommand2D;

public final class VulkanCommandEncoder {
    public VulkanDrawPacket encode(DrawCommand2D command) {
        return new VulkanDrawPacket(
                command.textureRef(),
                command.mesh(),
                command.clipMask(),
                command.renderState(),
                command.order().layer(),
                command.order().zIndex()
        );
    }
}
