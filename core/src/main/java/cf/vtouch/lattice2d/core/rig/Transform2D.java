package cf.vtouch.lattice2d.core.rig;

public record Transform2D(
        float translateX,
        float translateY,
        float rotationRad,
        float scaleX,
        float scaleY,
        float pivotX,
        float pivotY
) {
    private static final Transform2D IDENTITY = new Transform2D(0f, 0f, 0f, 1f, 1f, 0f, 0f);

    public static Transform2D identity() {
        return IDENTITY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public AffineTransform2D toAffine() {
        float c = (float) Math.cos(rotationRad);
        float s = (float) Math.sin(rotationRad);

        float m00 = c * scaleX;
        float m01 = -s * scaleY;
        float m10 = s * scaleX;
        float m11 = c * scaleY;

        float m02 = translateX + pivotX - m00 * pivotX - m01 * pivotY;
        float m12 = translateY + pivotY - m10 * pivotX - m11 * pivotY;

        return new AffineTransform2D(m00, m01, m02, m10, m11, m12);
    }

    public static final class Builder {
        private float translateX;
        private float translateY;
        private float rotationRad;
        private float scaleX = 1f;
        private float scaleY = 1f;
        private float pivotX;
        private float pivotY;

        public Builder translate(float x, float y) {
            this.translateX = x;
            this.translateY = y;
            return this;
        }

        public Builder rotationRad(float rotationRad) {
            this.rotationRad = rotationRad;
            return this;
        }

        public Builder scale(float x, float y) {
            this.scaleX = x;
            this.scaleY = y;
            return this;
        }

        public Builder pivot(float x, float y) {
            this.pivotX = x;
            this.pivotY = y;
            return this;
        }

        public Transform2D build() {
            return new Transform2D(translateX, translateY, rotationRad, scaleX, scaleY, pivotX, pivotY);
        }
    }
}
