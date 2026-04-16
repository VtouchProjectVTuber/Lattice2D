package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.geom.Mesh2D;

public final class ParameterAffineDeformer2D implements ParameterDeformer2D {
    private final String translateXParam;
    private final String translateYParam;
    private final String rotationParam;
    private final String scaleXParam;
    private final String scaleYParam;
    private final float translateXScale;
    private final float translateYScale;
    private final float rotationScaleRad;
    private final float scaleXScale;
    private final float scaleYScale;

    private ParameterAffineDeformer2D(Builder builder) {
        this.translateXParam = builder.translateXParam;
        this.translateYParam = builder.translateYParam;
        this.rotationParam = builder.rotationParam;
        this.scaleXParam = builder.scaleXParam;
        this.scaleYParam = builder.scaleYParam;
        this.translateXScale = builder.translateXScale;
        this.translateYScale = builder.translateYScale;
        this.rotationScaleRad = builder.rotationScaleRad;
        this.scaleXScale = builder.scaleXScale;
        this.scaleYScale = builder.scaleYScale;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Mesh2D apply(Mesh2D input, ParameterState2D state) {
        float tx = sampled(state, translateXParam) * translateXScale;
        float ty = sampled(state, translateYParam) * translateYScale;
        float rot = sampled(state, rotationParam) * rotationScaleRad;
        float sx = 1f + sampled(state, scaleXParam) * scaleXScale;
        float sy = 1f + sampled(state, scaleYParam) * scaleYScale;

        float c = (float) Math.cos(rot);
        float s = (float) Math.sin(rot);

        float m00 = c * sx;
        float m01 = -s * sy;
        float m10 = s * sx;
        float m11 = c * sy;

        return input.mapVertices((index, vertex) -> {
            float x = vertex.x();
            float y = vertex.y();
            float nx = m00 * x + m01 * y + tx;
            float ny = m10 * x + m11 * y + ty;
            return vertex.withPosition(nx, ny);
        });
    }

    private static float sampled(ParameterState2D state, String parameterId) {
        return parameterId == null ? 0f : state.value(parameterId);
    }

    public static final class Builder {
        private String translateXParam;
        private String translateYParam;
        private String rotationParam;
        private String scaleXParam;
        private String scaleYParam;
        private float translateXScale;
        private float translateYScale;
        private float rotationScaleRad;
        private float scaleXScale;
        private float scaleYScale;

        public Builder translateX(String parameterId, float scale) {
            this.translateXParam = parameterId;
            this.translateXScale = scale;
            return this;
        }

        public Builder translateY(String parameterId, float scale) {
            this.translateYParam = parameterId;
            this.translateYScale = scale;
            return this;
        }

        public Builder rotationRad(String parameterId, float scale) {
            this.rotationParam = parameterId;
            this.rotationScaleRad = scale;
            return this;
        }

        public Builder scaleX(String parameterId, float scale) {
            this.scaleXParam = parameterId;
            this.scaleXScale = scale;
            return this;
        }

        public Builder scaleY(String parameterId, float scale) {
            this.scaleYParam = parameterId;
            this.scaleYScale = scale;
            return this;
        }

        public ParameterAffineDeformer2D build() {
            return new ParameterAffineDeformer2D(this);
        }
    }
}
