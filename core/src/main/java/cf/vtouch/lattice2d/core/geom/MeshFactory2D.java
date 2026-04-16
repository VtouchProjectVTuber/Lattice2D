package cf.vtouch.lattice2d.core.geom;

import java.util.List;

public final class MeshFactory2D {
    private MeshFactory2D() {
    }

    public static Mesh2D quad(float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        return new Mesh2D(
                List.of(
                        new Vertex2D(x, y, 0f, 0f),
                        new Vertex2D(x2, y, 1f, 0f),
                        new Vertex2D(x2, y2, 1f, 1f),
                        new Vertex2D(x, y2, 0f, 1f)
                ),
                new int[]{0, 1, 2, 0, 2, 3}
        );
    }
}
