package cf.vtouch.lattice2d.core.scene;

import cf.vtouch.lattice2d.core.clip.ClipMask2D;
import cf.vtouch.lattice2d.core.deform.DeformPipeline2D;
import cf.vtouch.lattice2d.core.deform.Deformer2D;
import cf.vtouch.lattice2d.core.geom.Mesh2D;

import java.util.Objects;

public final class Drawable2D {
    private final String textureRef;
    private final Mesh2D mesh;
    private final DeformPipeline2D deformer;
    private final ClipMask2D clipMask;
    private final DrawOrder2D drawOrder;
    private final RenderState2D renderState;

    private Drawable2D(Builder builder) {
        this.textureRef = Objects.requireNonNull(builder.textureRef, "textureRef must not be null");
        this.mesh = Objects.requireNonNull(builder.mesh, "mesh must not be null");
        this.deformer = Objects.requireNonNull(builder.deformer, "deformer must not be null");
        this.clipMask = builder.clipMask;
        this.drawOrder = Objects.requireNonNull(builder.drawOrder, "drawOrder must not be null");
        this.renderState = Objects.requireNonNull(builder.renderState, "renderState must not be null");
    }

    public String textureRef() {
        return textureRef;
    }

    public Mesh2D sourceMesh() {
        return mesh;
    }

    public Mesh2D deformedMesh() {
        return deformer.apply(mesh);
    }

    public ClipMask2D clipMask() {
        return clipMask;
    }

    public DrawOrder2D drawOrder() {
        return drawOrder;
    }

    public RenderState2D renderState() {
        return renderState;
    }

    public static Builder builder(String textureRef, Mesh2D mesh) {
        return new Builder(textureRef, mesh);
    }

    public static final class Builder {
        private final String textureRef;
        private final Mesh2D mesh;
        private DeformPipeline2D deformer = new DeformPipeline2D().add(Deformer2D.identity());
        private ClipMask2D clipMask;
        private DrawOrder2D drawOrder = new DrawOrder2D(0, 0);
        private RenderState2D renderState = RenderState2D.defaultState();

        private Builder(String textureRef, Mesh2D mesh) {
            this.textureRef = textureRef;
            this.mesh = mesh;
        }

        public Builder deformer(DeformPipeline2D deformer) {
            this.deformer = deformer;
            return this;
        }

        public Builder clipMask(ClipMask2D clipMask) {
            this.clipMask = clipMask;
            return this;
        }

        public Builder drawOrder(DrawOrder2D drawOrder) {
            this.drawOrder = drawOrder;
            return this;
        }

        public Builder renderState(RenderState2D renderState) {
            this.renderState = renderState;
            return this;
        }

        public Drawable2D build() {
            return new Drawable2D(this);
        }
    }
}
