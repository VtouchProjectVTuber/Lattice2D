package cf.vtouch.lattice2d.core.geom;

public record Vertex2D(float x, float y, float u, float v) {
    public Vertex2D withPosition(float nx, float ny) {
        return new Vertex2D(nx, ny, u, v);
    }

    public Vertex2D withUv(float nu, float nv) {
        return new Vertex2D(x, y, nu, nv);
    }
}
