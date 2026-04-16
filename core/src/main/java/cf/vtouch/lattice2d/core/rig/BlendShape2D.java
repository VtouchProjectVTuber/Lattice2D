package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.Mesh2D;
import cf.vtouch.lattice2d.core.geom.Vertex2D;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class BlendShape2D {
    private final List<VertexDelta2D> deltas;

    public BlendShape2D(List<VertexDelta2D> deltas) {
        this.deltas = List.copyOf(Objects.requireNonNull(deltas, "deltas must not be null"));
    }

    public static BlendShape2D zeros(int vertexCount) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException("vertexCount must be >= 0");
        }
        VertexDelta2D[] values = new VertexDelta2D[vertexCount];
        Arrays.fill(values, new VertexDelta2D(0f, 0f));
        return new BlendShape2D(Arrays.asList(values));
    }

    public static Builder builder(int vertexCount) {
        return new Builder(vertexCount);
    }

    public int vertexCount() {
        return deltas.size();
    }

    public List<VertexDelta2D> deltas() {
        return deltas;
    }

    public Mesh2D apply(Mesh2D input, float weight) {
        if (input.vertices().size() != deltas.size()) {
            throw new IllegalArgumentException(
                    "BlendShape vertex count " + deltas.size() +
                            " does not match mesh vertex count " + input.vertices().size()
            );
        }
        return input.mapVertices((index, original) -> applyDelta(index, original, weight));
    }

    private Vertex2D applyDelta(int index, Vertex2D original, float weight) {
        VertexDelta2D delta = deltas.get(index);
        return original.withPosition(
                original.x() + delta.dx() * weight,
                original.y() + delta.dy() * weight
        );
    }

    public static final class Builder {
        private final VertexDelta2D[] deltas;

        private Builder(int vertexCount) {
            if (vertexCount < 0) {
                throw new IllegalArgumentException("vertexCount must be >= 0");
            }
            this.deltas = new VertexDelta2D[vertexCount];
            Arrays.fill(this.deltas, new VertexDelta2D(0f, 0f));
        }

        public Builder set(int vertexIndex, float dx, float dy) {
            if (vertexIndex < 0 || vertexIndex >= deltas.length) {
                throw new IndexOutOfBoundsException("vertexIndex out of range: " + vertexIndex);
            }
            deltas[vertexIndex] = new VertexDelta2D(dx, dy);
            return this;
        }

        public BlendShape2D build() {
            return new BlendShape2D(Arrays.asList(deltas.clone()));
        }
    }
}
