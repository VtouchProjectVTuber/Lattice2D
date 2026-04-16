package cf.vtouch.lattice2d.core.scene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SceneComposer2D {
    public List<DrawCommand2D> build(List<Drawable2D> drawables) {
        List<Drawable2D> sorted = new ArrayList<>(drawables);
        sorted.sort(Comparator.comparing(Drawable2D::drawOrder));

        List<DrawCommand2D> commands = new ArrayList<>(sorted.size());
        for (Drawable2D drawable : sorted) {
            commands.add(new DrawCommand2D(
                    drawable.textureRef(),
                    drawable.deformedMesh(),
                    drawable.clipMask(),
                    drawable.drawOrder(),
                    drawable.renderState()
            ));
        }
        return commands;
    }
}
