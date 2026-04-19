package cf.vtouch.lattice2d.vulkan;

import cf.vtouch.lattice2d.core.clip.ScissorClipMask2D;
import cf.vtouch.lattice2d.core.clip.StencilClipMask2D;
import cf.vtouch.lattice2d.core.geom.Mesh2D;
import cf.vtouch.lattice2d.core.geom.MeshFactory2D;
import cf.vtouch.lattice2d.core.scene.BlendMode2D;
import cf.vtouch.lattice2d.core.scene.ColorRgba2D;
import cf.vtouch.lattice2d.core.scene.DrawCommand2D;
import cf.vtouch.lattice2d.core.scene.DrawOrder2D;
import cf.vtouch.lattice2d.core.scene.FilterMode2D;
import cf.vtouch.lattice2d.core.scene.RenderState2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VulkanCommandEncoderTest {
    private final VulkanCommandEncoder encoder = new VulkanCommandEncoder();

    @Test
    void encodeMapsAllFieldsFromDrawCommand() {
        Mesh2D mesh = MeshFactory2D.quad(-8f, -8f, 16f, 16f);
        StencilClipMask2D clipMask = new StencilClipMask2D("face", 2, true, 0.25f);
        RenderState2D renderState = new RenderState2D(
                new ColorRgba2D(0.5f, 0.75f, 0.2f, 0.8f),
                0.65f,
                BlendMode2D.ADD,
                true,
                FilterMode2D.NEAREST,
                "mat.face"
        );
        DrawCommand2D command = new DrawCommand2D("tex.face", mesh, clipMask, new DrawOrder2D(4, 19), renderState);

        VulkanDrawPacket packet = encoder.encode(command);

        assertEquals("tex.face", packet.textureRef());
        assertEquals(mesh, packet.mesh());
        assertEquals(clipMask, packet.clipMask());
        assertEquals(renderState, packet.renderState());
        assertEquals(4, packet.layer());
        assertEquals(19, packet.zIndex());
    }

    @Test
    void encodeKeepsDefaultRenderStateWhenCommandUsesLegacyConstructor() {
        Mesh2D mesh = MeshFactory2D.quad(0f, 0f, 32f, 32f);
        ScissorClipMask2D clipMask = new ScissorClipMask2D(10f, 20f, 80f, 40f);
        DrawCommand2D command = new DrawCommand2D("tex.body", mesh, clipMask, new DrawOrder2D(1, 2));

        VulkanDrawPacket packet = encoder.encode(command);

        assertEquals(RenderState2D.defaultState(), packet.renderState());
        assertEquals("scissor", packet.clipMask().type());
        assertFalse(packet.renderState().premultipliedAlpha());
        assertTrue(packet.renderState().opacity() > 0.99f);
        assertNull(packet.renderState().materialId());
    }
}
