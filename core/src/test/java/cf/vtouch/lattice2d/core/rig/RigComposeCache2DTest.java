package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.MeshFactory2D;
import cf.vtouch.lattice2d.core.scene.DrawCommand2D;
import cf.vtouch.lattice2d.core.scene.DrawOrder2D;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class RigComposeCache2DTest {
    @Test
    void composeSortsDeterministicallyByDrawOrder() {
        Rig2D rig = Rig2D.builder()
                .addPart(RigPart2D.builder("face", "tex.face", MeshFactory2D.quad(0f, 0f, 10f, 10f))
                        .drawOrder(new DrawOrder2D(1, 5))
                        .build())
                .addPart(RigPart2D.builder("eye", "tex.eye", MeshFactory2D.quad(0f, 0f, 10f, 10f))
                        .drawOrder(new DrawOrder2D(0, 9))
                        .build())
                .addPart(RigPart2D.builder("hair", "tex.hair", MeshFactory2D.quad(0f, 0f, 10f, 10f))
                        .drawOrder(new DrawOrder2D(1, 0))
                        .build())
                .build();

        List<DrawCommand2D> commands = rig.compose(ParameterState2D.empty());
        assertEquals(List.of("tex.eye", "tex.hair", "tex.face"), commands.stream().map(DrawCommand2D::textureRef).toList());
    }

    @Test
    void cacheRebuildsOnlyDirtyPartsWhenDependenciesAreDeclared() {
        RigPart2D partA = RigPart2D.builder("a", "tex.a", MeshFactory2D.quad(0f, 0f, 10f, 10f))
                .deformer(new ParameterDeformPipeline2D().add(
                        ParameterAffineDeformer2D.builder().translateX("a", 5f).build()
                ))
                .dependsOn("a")
                .drawOrder(new DrawOrder2D(0, 0))
                .build();

        RigPart2D partB = RigPart2D.builder("b", "tex.b", MeshFactory2D.quad(0f, 0f, 10f, 10f))
                .deformer(new ParameterDeformPipeline2D().add(
                        ParameterAffineDeformer2D.builder().translateX("b", 5f).build()
                ))
                .dependsOn("b")
                .drawOrder(new DrawOrder2D(0, 1))
                .build();

        Rig2D rig = Rig2D.builder().addPart(partA).addPart(partB).build();
        RigComposeCache2D cache = rig.newCache();

        List<DrawCommand2D> frame0 = cache.compose(ParameterState2D.builder().set("a", 0f).set("b", 0f).build());
        List<DrawCommand2D> frame1 = cache.compose(ParameterState2D.builder().set("a", 1f).set("b", 0f).build());

        Map<String, DrawCommand2D> byTexture0 = indexByTexture(frame0);
        Map<String, DrawCommand2D> byTexture1 = indexByTexture(frame1);

        assertNotSame(byTexture0.get("tex.a"), byTexture1.get("tex.a"));
        assertSame(byTexture0.get("tex.b"), byTexture1.get("tex.b"));
    }

    private static Map<String, DrawCommand2D> indexByTexture(List<DrawCommand2D> commands) {
        Map<String, DrawCommand2D> map = new HashMap<>();
        for (DrawCommand2D command : commands) {
            map.put(command.textureRef(), command);
        }
        return map;
    }
}
