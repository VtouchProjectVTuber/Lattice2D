package cf.vtouch.lattice2d.core.clip;

public record RectClipMask2D(float x, float y, float width, float height) implements ClipMask2D {
    @Override
    public String type() {
        return "rect";
    }
}
