package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.clip.ClipMask2D;
import cf.vtouch.lattice2d.core.geom.Mesh2D;
import cf.vtouch.lattice2d.core.scene.DrawCommand2D;
import cf.vtouch.lattice2d.core.scene.DrawOrder2D;
import cf.vtouch.lattice2d.core.scene.RenderState2D;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class RigPart2D {
    private final String id;
    private final String parentId;
    private final String textureRef;
    private final Mesh2D mesh;
    private final ParameterDeformPipeline2D deformer;
    private final Transform2D localTransform;
    private final ClipMask2D clipMask;
    private final DrawOrder2D drawOrder;
    private final RenderState2D renderState;
    private final Set<String> parameterDependencies;

    private RigPart2D(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id must not be null");
        this.parentId = builder.parentId;
        this.textureRef = Objects.requireNonNull(builder.textureRef, "textureRef must not be null");
        this.mesh = Objects.requireNonNull(builder.mesh, "mesh must not be null");
        this.deformer = Objects.requireNonNull(builder.deformer, "deformer must not be null");
        this.localTransform = Objects.requireNonNull(builder.localTransform, "localTransform must not be null");
        this.clipMask = builder.clipMask;
        this.drawOrder = Objects.requireNonNull(builder.drawOrder, "drawOrder must not be null");
        this.renderState = Objects.requireNonNull(builder.renderState, "renderState must not be null");
        this.parameterDependencies = Set.copyOf(builder.parameterDependencies);
    }

    public String id() {
        return id;
    }

    public String textureRef() {
        return textureRef;
    }

    public String parentId() {
        return parentId;
    }

    public Mesh2D sourceMesh() {
        return mesh;
    }

    public Transform2D localTransform() {
        return localTransform;
    }

    public DrawOrder2D drawOrder() {
        return drawOrder;
    }

    public ClipMask2D clipMask() {
        return clipMask;
    }

    public RenderState2D renderState() {
        return renderState;
    }

    public Set<String> parameterDependencies() {
        return parameterDependencies;
    }

    public Mesh2D deformedMesh(ParameterState2D state) {
        return deformer.apply(mesh, state);
    }

    public DrawCommand2D toDrawCommand(ParameterState2D state) {
        return toDrawCommand(state, AffineTransform2D.identity());
    }

    public DrawCommand2D toDrawCommand(ParameterState2D state, AffineTransform2D worldTransform) {
        Mesh2D deformed = deformer.apply(mesh, state);
        Mesh2D transformed = worldTransform.apply(deformed);
        return new DrawCommand2D(
                textureRef,
                transformed,
                clipMask,
                drawOrder,
                renderState
        );
    }

    public static Builder builder(String id, String textureRef, Mesh2D mesh) {
        return new Builder(id, textureRef, mesh);
    }

    public static final class Builder {
        private final String id;
        private String parentId;
        private final String textureRef;
        private final Mesh2D mesh;
        private ParameterDeformPipeline2D deformer = new ParameterDeformPipeline2D().add(ParameterDeformer2D.identity());
        private Transform2D localTransform = Transform2D.identity();
        private ClipMask2D clipMask;
        private DrawOrder2D drawOrder = new DrawOrder2D(0, 0);
        private RenderState2D renderState = RenderState2D.defaultState();
        private final Set<String> parameterDependencies = new LinkedHashSet<>();

        private Builder(String id, String textureRef, Mesh2D mesh) {
            this.id = id;
            this.textureRef = textureRef;
            this.mesh = mesh;
        }

        public Builder deformer(ParameterDeformPipeline2D deformer) {
            this.deformer = deformer;
            return this;
        }

        public Builder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder localTransform(Transform2D localTransform) {
            this.localTransform = localTransform;
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

        public Builder dependsOn(String parameterId) {
            parameterDependencies.add(Objects.requireNonNull(parameterId, "parameterId must not be null"));
            return this;
        }

        public Builder dependsOn(String... parameterIds) {
            for (String parameterId : parameterIds) {
                dependsOn(parameterId);
            }
            return this;
        }

        public RigPart2D build() {
            return new RigPart2D(this);
        }
    }
}
