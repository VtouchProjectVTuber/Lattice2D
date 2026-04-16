package cf.vtouch.lattice2d.core.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Mesh2D {
    private final List<Vertex2D> vertices;
    private final int[] indices;

    public Mesh2D(List<Vertex2D> vertices, int[] indices) {
        this.vertices = List.copyOf(Objects.requireNonNull(vertices, "vertices must not be null"));
        this.indices = Objects.requireNonNull(indices, "indices must not be null").clone();
        validateIndexTopology();
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

    public int triangleCount() {
        return indices.length / 3;
    }

    public WindingOrder2D windingOfTriangle(int triangleIndex) {
        int base = triangleIndex * 3;
        if (base < 0 || base + 2 >= indices.length) {
            throw new IndexOutOfBoundsException("triangleIndex out of range: " + triangleIndex);
        }
        Vertex2D a = vertices.get(indices[base]);
        Vertex2D b = vertices.get(indices[base + 1]);
        Vertex2D c = vertices.get(indices[base + 2]);
        float signedAreaTwice = (b.x() - a.x()) * (c.y() - a.y()) - (b.y() - a.y()) * (c.x() - a.x());
        if (Math.abs(signedAreaTwice) <= 1.0e-7f) {
            return WindingOrder2D.DEGENERATE;
        }
        return signedAreaTwice > 0f ? WindingOrder2D.COUNTER_CLOCKWISE : WindingOrder2D.CLOCKWISE;
    }

    public List<Integer> findDegenerateTriangles() {
        List<Integer> degenerate = new ArrayList<>();
        for (int i = 0; i < triangleCount(); i++) {
            if (windingOfTriangle(i) == WindingOrder2D.DEGENERATE) {
                degenerate.add(i);
            }
        }
        return Collections.unmodifiableList(degenerate);
    }

    public MeshValidationReport2D validate(MeshValidationOptions2D options) {
        Objects.requireNonNull(options, "options must not be null");

        List<String> issues = new ArrayList<>();
        List<Integer> degenerateTriangles = new ArrayList<>();
        List<Integer> windingMismatchTriangles = new ArrayList<>();

        for (int i = 0; i < triangleCount(); i++) {
            WindingOrder2D winding = windingOfTriangle(i);
            if (winding == WindingOrder2D.DEGENERATE) {
                degenerateTriangles.add(i);
                if (options.rejectDegenerateTriangles()) {
                    issues.add("Triangle " + i + " is degenerate");
                }
                continue;
            }
            if (options.expectedWinding() != null && winding != options.expectedWinding()) {
                windingMismatchTriangles.add(i);
                issues.add("Triangle " + i + " has winding " + winding + ", expected " + options.expectedWinding());
            }
        }

        return new MeshValidationReport2D(
                issues.isEmpty(),
                Collections.unmodifiableList(issues),
                Collections.unmodifiableList(degenerateTriangles),
                Collections.unmodifiableList(windingMismatchTriangles)
        );
    }

    private void validateIndexTopology() {
        if (indices.length % 3 != 0) {
            throw new IllegalArgumentException("indices length must be a multiple of 3");
        }
        for (int index : indices) {
            if (index < 0 || index >= vertices.size()) {
                throw new IllegalArgumentException(
                        "index out of range: " + index + " for vertices size " + vertices.size()
                );
            }
        }
    }

    @FunctionalInterface
    public interface VertexMapper {
        Vertex2D map(int index, Vertex2D original);
    }
}
