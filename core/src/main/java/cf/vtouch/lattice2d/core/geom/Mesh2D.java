package cf.vtouch.lattice2d.core.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Mesh2D {
    private final List<Vertex2D> vertices;
    private final int[] indices;

    public Mesh2D(List<Vertex2D> vertices, int[] indices) {
        this.vertices = List.copyOf(Objects.requireNonNull(vertices, "vertices must not be null"));
        this.indices = Objects.requireNonNull(indices, "indices must not be null").clone();
    }

    public List<Vertex2D> vertices() {
        return vertices;
    }

    public int[] indices() {
        return indices.clone();
    }

    public Mesh2D mapVertices(VertexMapper mapper) {
        List<Vertex2D> mapped = new ArrayList<>(vertices.size());
        for (int i = 0; i < vertices.size(); i++) {
            mapped.add(mapper.map(i, vertices.get(i)));
        }
        return new Mesh2D(mapped, indices);
    }

    @FunctionalInterface
    public interface VertexMapper {
        Vertex2D map(int index, Vertex2D original);
    }
}
