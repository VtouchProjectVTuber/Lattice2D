package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.scene.DrawCommand2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class Rig2D {
    private final List<RigPart2D> parts;
    private final List<RigPart2D> orderedParts;
    private final Map<String, RigPart2D> partById;
    private final Map<String, AffineTransform2D> worldTransforms;

    public Rig2D(List<RigPart2D> parts) {
        this.parts = List.copyOf(Objects.requireNonNull(parts, "parts must not be null"));
        this.partById = indexPartsById(this.parts);
        this.worldTransforms = computeWorldTransforms(this.partById);
        this.orderedParts = sortByDrawOrder(this.parts);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<RigPart2D> parts() {
        return parts;
    }

    public List<DrawCommand2D> compose(ParameterState2D state) {
        List<DrawCommand2D> commands = new ArrayList<>(orderedParts.size());
        for (RigPart2D part : orderedParts) {
            commands.add(part.toDrawCommand(state, worldTransforms.get(part.id())));
        }
        return commands;
    }

    public List<DrawCommand2D> compose() {
        return compose(ParameterState2D.empty());
    }

    public RigComposeCache2D newCache() {
        return new RigComposeCache2D(this);
    }

    List<RigPart2D> orderedParts() {
        return orderedParts;
    }

    AffineTransform2D worldTransform(String partId) {
        return worldTransforms.get(partId);
    }

    private static List<RigPart2D> sortByDrawOrder(List<RigPart2D> parts) {
        List<RigPart2D> sorted = new ArrayList<>(parts);
        sorted.sort(Comparator.comparing(RigPart2D::drawOrder));
        return Collections.unmodifiableList(sorted);
    }

    private static Map<String, RigPart2D> indexPartsById(List<RigPart2D> parts) {
        Map<String, RigPart2D> index = new LinkedHashMap<>();
        for (RigPart2D part : parts) {
            RigPart2D previous = index.put(part.id(), part);
            if (previous != null) {
                throw new IllegalArgumentException("Duplicate part id: " + part.id());
            }
        }
        return Collections.unmodifiableMap(index);
    }

    private static Map<String, AffineTransform2D> computeWorldTransforms(Map<String, RigPart2D> partById) {
        Map<String, AffineTransform2D> world = new HashMap<>();
        Set<String> visiting = new HashSet<>();
        for (String partId : partById.keySet()) {
            resolveWorldTransform(partId, partById, world, visiting);
        }
        return Collections.unmodifiableMap(world);
    }

    private static AffineTransform2D resolveWorldTransform(
            String partId,
            Map<String, RigPart2D> partById,
            Map<String, AffineTransform2D> world,
            Set<String> visiting
    ) {
        AffineTransform2D cached = world.get(partId);
        if (cached != null) {
            return cached;
        }
        if (!visiting.add(partId)) {
            throw new IllegalArgumentException("Rig hierarchy cycle detected at part id: " + partId);
        }

        RigPart2D part = partById.get(partId);
        if (part == null) {
            throw new IllegalArgumentException("Unknown part id: " + partId);
        }

        AffineTransform2D local = part.localTransform().toAffine();
        AffineTransform2D resolved;
        String parentId = part.parentId();
        if (parentId == null) {
            resolved = local;
        } else {
            RigPart2D parent = partById.get(parentId);
            if (parent == null) {
                throw new IllegalArgumentException(
                        "Unknown parent id '" + parentId + "' for part id '" + part.id() + "'"
                );
            }
            AffineTransform2D parentWorld = resolveWorldTransform(parent.id(), partById, world, visiting);
            resolved = parentWorld.combine(local);
        }

        visiting.remove(partId);
        world.put(partId, resolved);
        return resolved;
    }

    public static final class Builder {
        private final List<RigPart2D> parts = new ArrayList<>();

        public Builder addPart(RigPart2D part) {
            parts.add(Objects.requireNonNull(part, "part must not be null"));
            return this;
        }

        public Rig2D build() {
            return new Rig2D(parts);
        }
    }
}
