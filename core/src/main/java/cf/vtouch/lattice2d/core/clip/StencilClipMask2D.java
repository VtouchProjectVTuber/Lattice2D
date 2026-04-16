package cf.vtouch.lattice2d.core.clip;

import java.util.Objects;

public record StencilClipMask2D(String stencilId, int group, boolean inverted, float feather) implements ClipMask2D {
    public StencilClipMask2D {
        stencilId = Objects.requireNonNull(stencilId, "stencilId must not be null");
        if (group < 0) {
            throw new IllegalArgumentException("group must be >= 0");
        }
        if (feather < 0f) {
            throw new IllegalArgumentException("feather must be >= 0");
        }
    }

    @Override
    public String type() {
        return "stencil";
    }
}
