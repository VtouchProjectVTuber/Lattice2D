package cf.vtouch.lattice2d.core.clip;

public record ScissorClipMask2D(float x, float y, float width, float height) implements ClipMask2D {
    public ScissorClipMask2D {
        if (width < 0f || height < 0f) {
            throw new IllegalArgumentException("width and height must be >= 0");
        }
    }

    @Override
    public String type() {
        return "scissor";
    }
}
