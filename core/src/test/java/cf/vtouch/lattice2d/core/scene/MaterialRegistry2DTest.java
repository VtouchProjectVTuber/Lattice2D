package cf.vtouch.lattice2d.core.scene;

import cf.vtouch.lattice2d.core.geom.MeshFactory2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MaterialRegistry2DTest {
    @Test
    void resolveUsesRegisteredSlotsAndFallbackBaseColor() {
        MaterialRegistry2D registry = MaterialRegistry2D.builder()
                .add(MaterialDefinition2D.builder("toon")
                        .texture(TextureSlot2D.NORMAL, "tex.normal")
                        .build())
                .build();

        DrawCommand2D command = new DrawCommand2D(
                "tex.base",
                MeshFactory2D.quad(0f, 0f, 10f, 10f),
                null,
                new DrawOrder2D(0, 0),
                new RenderState2D(ColorRgba2D.WHITE, 1f, BlendMode2D.NORMAL, false, FilterMode2D.LINEAR, "toon")
        );

        ResolvedMaterial2D resolved = registry.resolve(command);
        assertEquals("toon", resolved.materialId());
        assertEquals("tex.base", resolved.material().textureRef(TextureSlot2D.BASE_COLOR));
        assertEquals("tex.normal", resolved.material().textureRef(TextureSlot2D.NORMAL));
    }

    @Test
    void resolveFallsBackToInlineMaterialWhenNoMaterialId() {
        MaterialRegistry2D registry = MaterialRegistry2D.builder().build();
        DrawCommand2D command = new DrawCommand2D(
                "tex.base",
                MeshFactory2D.quad(0f, 0f, 10f, 10f),
                null,
                new DrawOrder2D(0, 0)
        );

        ResolvedMaterial2D resolved = registry.resolve(command);
        assertEquals("__inline", resolved.materialId());
        assertEquals("tex.base", resolved.material().textureRef(TextureSlot2D.BASE_COLOR));
        assertNull(resolved.material().textureRef(TextureSlot2D.NORMAL));
    }
}
