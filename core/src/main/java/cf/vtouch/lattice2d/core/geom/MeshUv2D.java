package cf.vtouch.lattice2d.core.geom;

public final class MeshUv2D {
    private MeshUv2D() {
    }

    public static Mesh2D transform(Mesh2D input, UvTransform2D transform) {
        return transform.apply(input);
    }

    public static Mesh2D remapToRegion(Mesh2D input, UvRect2D region) {
        return UvTransform2D.fromRegion(region).apply(input);
    }

    public static UvRect2D spriteSheetRegion(int columns, int rows, int columnIndex, int rowIndex) {
        if (columns <= 0 || rows <= 0) {
            throw new IllegalArgumentException("columns and rows must be > 0");
        }
        if (columnIndex < 0 || columnIndex >= columns) {
            throw new IllegalArgumentException("columnIndex out of range: " + columnIndex);
        }
        if (rowIndex < 0 || rowIndex >= rows) {
            throw new IllegalArgumentException("rowIndex out of range: " + rowIndex);
        }

        float cellW = 1f / columns;
        float cellH = 1f / rows;
        float u0 = columnIndex * cellW;
        float v0 = rowIndex * cellH;
        return new UvRect2D(u0, v0, u0 + cellW, v0 + cellH);
    }
}
