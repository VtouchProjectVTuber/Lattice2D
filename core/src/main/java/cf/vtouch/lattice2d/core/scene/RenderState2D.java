package cf.vtouch.lattice2d.core.scene;

import java.util.Objects;

public record RenderState2D(
        ColorRgba2D tint,
        float opacity,
        BlendMode2D blendMode,
        boolean premultipliedAlpha,
        FilterMode2D filterMode,
        String materialId
) {
    private static final RenderState2D DEFAULT = new RenderState2D(
            ColorRgba2D.WHITE,
            1f,
            BlendMode2D.NORMAL,
            false,
            FilterMode2D.LINEAR,
            null
    );

    public RenderState2D {
        tint = Objects.requireNonNull(tint, "tint must not be null");
        opacity = clamp01(opacity);
        blendMode = Objects.requireNonNull(blendMode, "blendMode must not be null");
        filterMode = Objects.requireNonNull(filterMode, "filterMode must not be null");
    }

    public static RenderState2D defaultState() {
        return DEFAULT;
    }

    private static float clamp01(float value) {
        return Math.max(0f, Math.min(1f, value));
    }
}
