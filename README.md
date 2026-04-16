# Lattice2D (Java)

Toolkit helper for 2D mesh/deformer/clip/draw-order orchestration.
This project intentionally does not render by itself; your app controls GPU rendering.

## Build and Deploy

Build and verify all modules:

```powershell
mvn clean verify
```

Deploy to Maven Central (uses `serverId=central` from your `settings.xml`):

```powershell
mvn clean deploy
```

Deploy without waiting for Central publish completion:

```powershell
mvn clean deploy "-Dcentral.autoPublish=false" "-Dcentral.waitUntil=validated"
```

PowerShell note: quote `-D` properties that contain dots.

Important publishing layout:

- `lattice2d-lib`, `lattice2d-opengl`, `lattice2d-vulkan`, `lattice2d-dx` are standalone publishable artifacts with their own complete POM metadata.
- Root `pom.xml` is an aggregator (`cf.vtouch.build:lattice2d-build`) and is configured to skip deploy.
- The primary runtime dependency should be `cf.vtouch:lattice2d-lib`.
- `cf.vtouch:lattice2d` is a relocation POM that forwards to `cf.vtouch:lattice2d-lib`.

## Recommended dependency

Use `lattice2d-lib` as the main runtime artifact:

```xml
<dependency>
  <groupId>cf.vtouch</groupId>
  <artifactId>lattice2d-lib</artifactId>
  <version>0.1.0-alpha.1</version>
</dependency>
```

## Modules

- `core`: data model and composition pipeline (`Texture + Mesh + Deformer + ClippingMask + DrawOrder`) plus parameter-driven rigging helpers
- `backend-opengl`: helper encoder for OpenGL-facing command packets
- `backend-vulkan`: helper encoder for Vulkan-facing command packets
- `backend-dx`: helper encoder for DirectX-facing command packets

## Simple rigging app flow

Use `lattice2d-lib` as a pure data/composition layer and let your app render with any GPU backend.

```java
import cf.vtouch.lattice2d.core.geom.MeshFactory2D;
import cf.vtouch.lattice2d.core.scene.DrawCommand2D;
import cf.vtouch.lattice2d.core.scene.DrawOrder2D;
import cf.vtouch.lattice2d.core.rig.*;

import java.util.List;

public final class RigFrameExample {
    public List<DrawCommand2D> buildFrame(float globalRotation, float channelA) {
        BlendShape2D shape = BlendShape2D.builder(4)
                .set(2, 0f, 6f)
                .set(3, 0f, 6f)
                .build();

        BlendShapeParameterDeformer2D blend = new BlendShapeParameterDeformer2D()
                .channel(StandardParameterIds2D.CHANNEL_A, shape);

        ParameterAffineDeformer2D affine = ParameterAffineDeformer2D.builder()
                .rotationRad(StandardParameterIds2D.GLOBAL_ROTATION, (float) Math.toRadians(15))
                .build();

        ParameterDeformPipeline2D pipeline = new ParameterDeformPipeline2D()
                .add(affine)
                .add(blend);

        RigPart2D part = RigPart2D.builder(
                        "main",
                        "tex.main",
                        MeshFactory2D.quad(-128f, -128f, 256f, 256f)
                )
                .deformer(pipeline)
                .drawOrder(new DrawOrder2D(0, 0))
                .build();

        Rig2D rig = Rig2D.builder()
                .addPart(part)
                .build();

        ParameterState2D state = ParameterState2D.builder()
                .set(StandardParameterIds2D.GLOBAL_ROTATION, globalRotation)
                .set(StandardParameterIds2D.CHANNEL_A, channelA)
                .build();

        return rig.compose(state);
    }
}
```

`rig.compose(state)` returns `List<DrawCommand2D>` that your renderer can send to OpenGL, Vulkan, DirectX, or other pipelines.

## Advanced 2D capabilities

- Render state in `DrawCommand2D`: `RenderState2D` with tint, opacity, blend mode, premultiplied alpha, filter mode, and `materialId`
- Clip masks: rectangle (`RectClipMask2D`), scissor (`ScissorClipMask2D`), stencil (`StencilClipMask2D`)
- Hierarchical rig transforms: parent-child + local transform + pivot/origin via `Transform2D`
- Cached compose path: `RigComposeCache2D` with dirty updates based on parameter dependencies
- Parameter specs: `ParameterSpec2D` with min/max/default/clamp/curve and helpers in `ParameterMath2D` (`lerp`, `smoothStep`, `spring`)
- Mesh safety and validation: index-range checks, triangle winding checks, and degenerate triangle detection

Cache compose example:

```java
RigComposeCache2D cache = rig.newCache();
List<DrawCommand2D> frame = cache.compose(state);
```

## Artifact IDs

- `core` module => `cf.vtouch:lattice2d-lib`
- `backend-opengl` => `cf.vtouch:lattice2d-opengl`
- `backend-vulkan` => `cf.vtouch:lattice2d-vulkan`
- `backend-dx` => `cf.vtouch:lattice2d-dx`

## Backend runtime deps

- Default: no native runtime dependency (pure Java adapters)
- Optional `with-glfw` profile: adds GLFW binding (`io.github.over-run:overrungl-glfw`)
- Optional `with-directjlib` profile in `backend-dx`: adds DirectJLib

DirectJLib in `backend-dx` is wired as an optional Maven profile named `with-directjlib`.
If your DirectJLib is already published, pass its version:

```powershell
mvn -q -pl backend-dx -Pwith-directjlib "-Ddirectjlib.version=1.0.0" clean package
```

Enable GLFW profile for any backend module:

```powershell
mvn -q -pl backend-opengl -Pwith-glfw clean package
```

If you have a local `directjlib.jar`, install it once to local Maven repo:

```powershell
mvn install:install-file `
  -DgroupId=cf.vtouch.thirdparty `
  -DartifactId=directjlib `
  -Dversion=1.0.0 `
  -Dpackaging=jar `
  -Dfile="C:\path\to\directjlib.jar"
```

## Suggested package naming

Current package family:

- `cf.vtouch.lattice2d.core`
- `cf.vtouch.lattice2d.opengl`
- `cf.vtouch.lattice2d.vulkan`
- `cf.vtouch.lattice2d.dx`

Alternative names you can switch to later:

- `cf.vtouch.meshflow.*`
- `cf.vtouch.canvas2d.*`
- `cf.vtouch.morph2d.*`
