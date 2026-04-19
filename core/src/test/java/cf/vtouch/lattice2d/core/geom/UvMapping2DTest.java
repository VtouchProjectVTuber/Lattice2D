package cf.vtouch.lattice2d.core.geom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UvMapping2DTest {
    @Test
    void remapToAtlasRegionScalesAndOffsetsUvs() {
        Mesh2D mesh = MeshFactory2D.quad(0f, 0f, 10f, 10f);
        Mesh2D remapped = MeshUv2D.remapToRegion(mesh, new UvRect2D(0.25f, 0.5f, 0.75f, 1f));

        assertEquals(0.25f, remapped.vertices().get(0).u(), 1.0e-6f);
        assertEquals(0.5f, remapped.vertices().get(0).v(), 1.0e-6f);
        assertEquals(0.75f, remapped.vertices().get(2).u(), 1.0e-6f);
        assertEquals(1f, remapped.vertices().get(2).v(), 1.0e-6f);
    }

    @Test
    void uvTransformSupportsRotateScaleAndOffset() {
        Mesh2D mesh = new Mesh2D(
                java.util.List.of(new Vertex2D(0f, 0f, 1f, 0f)),
                new int[]{0, 0, 0}
        );
        UvTransform2D transform = UvTransform2D.translate(0.1f, 0.2f)
                .combine(UvTransform2D.scale(0.5f, 0.25f))
                .combine(UvTransform2D.rotateRad((float) Math.PI));

        Mesh2D out = MeshUv2D.transform(mesh, transform);
        assertEquals(-0.4f, out.vertices().get(0).u(), 1.0e-6f);
        assertEquals(0.2f, out.vertices().get(0).v(), 1.0e-6f);
    }

    @Test
    void spriteSheetRegionComputesExpectedCell() {
        UvRect2D region = MeshUv2D.spriteSheetRegion(4, 2, 1, 1);
        assertEquals(0.25f, region.u0(), 1.0e-6f);
        assertEquals(0.5f, region.v0(), 1.0e-6f);
        assertEquals(0.5f, region.u1(), 1.0e-6f);
        assertEquals(1f, region.v1(), 1.0e-6f);
    }

    @Test
    void spriteSheetRegionRejectsOutOfRangeIndices() {
        assertThrows(IllegalArgumentException.class, () -> MeshUv2D.spriteSheetRegion(0, 2, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> MeshUv2D.spriteSheetRegion(2, 2, 2, 0));
        assertThrows(IllegalArgumentException.class, () -> MeshUv2D.spriteSheetRegion(2, 2, 0, 2));
    }
}
