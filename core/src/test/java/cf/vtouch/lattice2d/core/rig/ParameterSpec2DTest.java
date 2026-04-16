package cf.vtouch.lattice2d.core.rig;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParameterSpec2DTest {
    @Test
    void resolvesWithClampAndDefault() {
        ParameterSpec2D spec = ParameterSpec2D.linear("x", -1f, 1f, 0.25f);

        ParameterState2D empty = ParameterState2D.empty();
        assertEquals(0.25f, empty.value(spec), 1.0e-6f);

        ParameterState2D outOfRange = ParameterState2D.builder().set("x", 2f).build();
        assertEquals(1f, outOfRange.value(spec), 1.0e-6f);
    }

    @Test
    void resolvesWithWrapAndCurve() {
        ParameterSpec2D wrapped = new ParameterSpec2D(
                "phase",
                0f,
                10f,
                0f,
                ParameterClampMode2D.WRAP,
                ParameterCurve2D.LINEAR
        );
        ParameterState2D state = ParameterState2D.builder().set("phase", 12.5f).build();
        assertEquals(2.5f, state.value(wrapped), 1.0e-6f);

        ParameterSpec2D curved = new ParameterSpec2D(
                "curve",
                0f,
                1f,
                0f,
                ParameterClampMode2D.CLAMP,
                ParameterCurve2D.SMOOTHSTEP
        );
        ParameterState2D mid = ParameterState2D.builder().set("curve", 0.25f).build();
        float value = mid.value(curved);
        assertTrue(value > 0.1f && value < 0.2f);
    }

    @Test
    void mathUtilitiesProduceStableValues() {
        assertEquals(5f, ParameterMath2D.lerp(0f, 10f, 0.5f), 1.0e-6f);
        assertEquals(0f, ParameterMath2D.smoothStep(0f, 10f, -1f), 1.0e-6f);
        assertEquals(1f, ParameterMath2D.smoothStep(0f, 10f, 11f), 1.0e-6f);

        ParameterMath2D.SpringResult2D spring = ParameterMath2D.spring(0f, 1f, 0f, 40f, 8f, 1f / 60f);
        assertTrue(spring.value() > 0f);
        assertTrue(spring.velocity() > 0f);
    }
}
