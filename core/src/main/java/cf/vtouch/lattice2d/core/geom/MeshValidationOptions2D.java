package cf.vtouch.lattice2d.core.geom;

public record MeshValidationOptions2D(
        boolean rejectDegenerateTriangles,
        WindingOrder2D expectedWinding
) {
    public static MeshValidationOptions2D defaults() {
        return new MeshValidationOptions2D(false, null);
    }
}
