package cf.vtouch.lattice2d.core.geom;

import java.util.List;

public record MeshValidationReport2D(
        boolean valid,
        List<String> issues,
        List<Integer> degenerateTriangles,
        List<Integer> windingMismatchTriangles
) {
}
