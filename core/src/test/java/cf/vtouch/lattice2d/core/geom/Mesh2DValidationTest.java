package cf.vtouch.lattice2d.core.geom;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Mesh2DValidationTest {
    @Test
    void rejectsOutOfRangeIndices() {
        List<Vertex2D> vertices = List.of(
                new Vertex2D(0f, 0f, 0f, 0f),
                new Vertex2D(1f, 0f, 1f, 0f),
                new Vertex2D(0f, 1f, 0f, 1f)
        );
        assertThrows(IllegalArgumentException.class, () -> new Mesh2D(vertices, new int[]{0, 1, 3}));
    }

    @Test
    void detectsDegenerateAndWindingMismatch() {
        Mesh2D mesh = new Mesh2D(
                List.of(
                        new Vertex2D(0f, 0f, 0f, 0f),
                        new Vertex2D(1f, 0f, 1f, 0f),
                        new Vertex2D(0f, 1f, 0f, 1f),
                        new Vertex2D(2f, 0f, 1f, 0f)
                ),
                new int[]{0, 1, 2, 1, 3, 3}
        );

        assertEquals(2, mesh.triangleCount());
        assertEquals(WindingOrder2D.COUNTER_CLOCKWISE, mesh.windingOfTriangle(0));
        assertEquals(WindingOrder2D.DEGENERATE, mesh.windingOfTriangle(1));

        MeshValidationReport2D report = mesh.validate(new MeshValidationOptions2D(true, WindingOrder2D.CLOCKWISE));
        assertTrue(report.degenerateTriangles().contains(1));
        assertTrue(report.windingMismatchTriangles().contains(0));
        assertTrue(report.issues().size() >= 2);
    }
}
