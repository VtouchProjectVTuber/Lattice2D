package cf.vtouch.lattice2d.core.scene;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class MaterialRegistry2D {
    private final Map<String, MaterialDefinition2D> byId;

    private MaterialRegistry2D(Map<String, MaterialDefinition2D> byId) {
        this.byId = Map.copyOf(byId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public MaterialDefinition2D find(String materialId) {
        return byId.get(materialId);
    }

    public ResolvedMaterial2D resolve(DrawCommand2D command) {
        Objects.requireNonNull(command, "command must not be null");

        String requestedMaterialId = command.renderState() != null ? command.renderState().materialId() : null;
        if (requestedMaterialId == null || requestedMaterialId.isBlank()) {
            MaterialDefinition2D fallback = MaterialDefinition2D.builder("__inline")
                    .baseColor(command.textureRef())
                    .build();
            return new ResolvedMaterial2D("__inline", fallback);
        }

        MaterialDefinition2D registered = byId.get(requestedMaterialId);
        if (registered == null) {
            MaterialDefinition2D fallback = MaterialDefinition2D.builder(requestedMaterialId)
                    .baseColor(command.textureRef())
                    .build();
            return new ResolvedMaterial2D(requestedMaterialId, fallback);
        }

        return new ResolvedMaterial2D(requestedMaterialId, registered.withFallbackBaseTexture(command.textureRef()));
    }

    public static final class Builder {
        private final LinkedHashMap<String, MaterialDefinition2D> byId = new LinkedHashMap<>();

        public Builder add(MaterialDefinition2D material) {
            Objects.requireNonNull(material, "material must not be null");
            byId.put(material.id(), material);
            return this;
        }

        public Builder add(String id, MaterialDefinition2D material) {
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(material, "material must not be null");
            byId.put(id, material);
            return this;
        }

        public MaterialRegistry2D build() {
            return new MaterialRegistry2D(byId);
        }
    }
}
