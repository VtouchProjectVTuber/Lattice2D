package cf.vtouch.lattice2d.core.scene;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class MaterialDefinition2D {
    private final String id;
    private final Map<String, String> textureBySlot;

    private MaterialDefinition2D(String id, Map<String, String> textureBySlot) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.textureBySlot = Map.copyOf(Objects.requireNonNull(textureBySlot, "textureBySlot must not be null"));
    }

    public String id() {
        return id;
    }

    public String textureRef(String slot) {
        return textureBySlot.get(slot);
    }

    public Map<String, String> textureBySlot() {
        return textureBySlot;
    }

    public MaterialDefinition2D withFallbackBaseTexture(String textureRef) {
        if (textureBySlot.containsKey(TextureSlot2D.BASE_COLOR) || textureRef == null) {
            return this;
        }
        LinkedHashMap<String, String> merged = new LinkedHashMap<>(textureBySlot);
        merged.put(TextureSlot2D.BASE_COLOR, textureRef);
        return new MaterialDefinition2D(id, merged);
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final String id;
        private final LinkedHashMap<String, String> textureBySlot = new LinkedHashMap<>();

        private Builder(String id) {
            this.id = id;
        }

        public Builder texture(String slot, String textureRef) {
            textureBySlot.put(requireNotBlank(slot, "slot"), requireNotBlank(textureRef, "textureRef"));
            return this;
        }

        public Builder baseColor(String textureRef) {
            return texture(TextureSlot2D.BASE_COLOR, textureRef);
        }

        public MaterialDefinition2D build() {
            if (textureBySlot.isEmpty()) {
                throw new IllegalArgumentException("material must contain at least one texture slot");
            }
            return new MaterialDefinition2D(requireNotBlank(id, "id"), textureBySlot);
        }

        private static String requireNotBlank(String value, String field) {
            Objects.requireNonNull(value, field + " must not be null");
            if (value.isBlank()) {
                throw new IllegalArgumentException(field + " must not be blank");
            }
            return value;
        }
    }
}
