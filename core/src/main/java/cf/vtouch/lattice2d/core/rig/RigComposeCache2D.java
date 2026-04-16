package cf.vtouch.lattice2d.core.rig;

import cf.vtouch.lattice2d.core.scene.DrawCommand2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RigComposeCache2D {
    private final Rig2D rig;
    private final Map<String, DrawCommand2D> commandCache = new HashMap<>();
    private ParameterState2D lastState = ParameterState2D.empty();

    public RigComposeCache2D(Rig2D rig) {
        this.rig = rig;
    }

    public List<DrawCommand2D> compose(ParameterState2D state) {
        Set<String> changedKeys = diffKeys(lastState.values(), state.values());

        for (RigPart2D part : rig.orderedParts()) {
            DrawCommand2D existing = commandCache.get(part.id());
            if (!isDirty(part, existing, changedKeys)) {
                continue;
            }

            DrawCommand2D command = part.toDrawCommand(state, rig.worldTransform(part.id()));
            commandCache.put(part.id(), command);
        }

        List<DrawCommand2D> ordered = new ArrayList<>(rig.orderedParts().size());
        for (RigPart2D part : rig.orderedParts()) {
            ordered.add(commandCache.get(part.id()));
        }

        lastState = state;
        return Collections.unmodifiableList(ordered);
    }

    private static boolean isDirty(RigPart2D part, DrawCommand2D existing, Set<String> changedKeys) {
        if (existing == null) {
            return true;
        }
        if (changedKeys.isEmpty()) {
            return false;
        }

        Set<String> dependencies = part.parameterDependencies();
        if (dependencies.isEmpty()) {
            return true;
        }
        for (String changedKey : changedKeys) {
            if (dependencies.contains(changedKey)) {
                return true;
            }
        }
        return false;
    }

    private static Set<String> diffKeys(Map<String, Float> previous, Map<String, Float> current) {
        Set<String> changed = new HashSet<>();
        for (Map.Entry<String, Float> entry : previous.entrySet()) {
            String key = entry.getKey();
            Float currentValue = current.get(key);
            if (currentValue == null || Float.compare(currentValue, entry.getValue()) != 0) {
                changed.add(key);
            }
        }
        for (Map.Entry<String, Float> entry : current.entrySet()) {
            String key = entry.getKey();
            Float previousValue = previous.get(key);
            if (previousValue == null || Float.compare(previousValue, entry.getValue()) != 0) {
                changed.add(key);
            }
        }
        return changed;
    }
}
