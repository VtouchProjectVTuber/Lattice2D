package cf.vtouch.lattice2d.core.scene;

public record DrawOrder2D(int layer, int zIndex) implements Comparable<DrawOrder2D> {
    @Override
    public int compareTo(DrawOrder2D other) {
        int layerComp = Integer.compare(layer, other.layer);
        if (layerComp != 0) {
            return layerComp;
        }
        return Integer.compare(zIndex, other.zIndex);
    }
}
