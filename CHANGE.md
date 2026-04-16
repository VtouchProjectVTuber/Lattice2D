# CHANGELOG

## 0.1.0-alpha.1

- Added a parameter-driven rigging layer under `cf.vtouch.lattice2d.core.rig`.
- Added parameter model and deformation flow:
  - `ParameterState2D`
  - `ParameterDeformer2D`
  - `ParameterDeformPipeline2D`
  - `ParameterAffineDeformer2D`
- Added blendshape workflow for real-time parameter channels:
  - `VertexDelta2D`
  - `BlendShape2D`
  - `BlendShapeParameterDeformer2D`
- Added rig composition primitives:
  - `RigPart2D`
  - `Rig2D`
  - `StandardParameterIds2D`
- Added `MeshFactory2D.quad(...)` for fast sprite-part setup in simple render pipelines.
- Added render-state support on draw commands (`RenderState2D`, blend mode, filter mode, tint/opacity, material id).
- Added clipping options for scissor/stencil masks (`ScissorClipMask2D`, `StencilClipMask2D`).
- Added hierarchy transform support in rig parts with parent-child and pivot/origin (`Transform2D`).
- Added cached compose flow with dirty updates (`RigComposeCache2D`, part parameter dependencies).
- Added parameter specification utilities (`ParameterSpec2D`, clamp modes, curves, and `ParameterMath2D` helpers).
- Added mesh validation toolkit (index topology checks, winding analysis, degenerate detection).
- Added unit tests for deterministic draw ordering, parameter/deformer behavior, and mesh validation.
- Changed Maven publish structure: every module now has its own standalone POM metadata (no parent inheritance in published artifacts).
- Switched root project to build-only aggregator (`cf.vtouch.build:lattice2d-build`) with deploy skipped, so `cf.vtouch:lattice2d` is no longer published.
- Set `cf.vtouch:lattice2d-lib` as the primary runtime artifact.
- Added `cf.vtouch:lattice2d` relocation POM that points to `cf.vtouch:lattice2d-lib`.
- Bumped project version to `0.1.0-alpha.1`.

## 0.1.0-alpha.0

- Initial Maven multi-module release for `cf.vtouch:lattice2d`.
- Published modules:
  - `cf.vtouch:lattice2d` (parent POM)
  - `cf.vtouch:lattice2d-lib`
  - `cf.vtouch:lattice2d-opengl`
  - `cf.vtouch:lattice2d-vulkan`
  - `cf.vtouch:lattice2d-dx`
- Added 2D pipeline foundation:
  - Mesh geometry (`Mesh2D`, `Vertex2D`)
  - Deformer pipeline (`Deformer2D`, `DeformPipeline2D`, `AffineDeformer2D`)
  - Clipping mask abstraction (`ClipMask2D`, `RectClipMask2D`)
  - Draw order and composition (`DrawOrder2D`, `Drawable2D`, `DrawCommand2D`, `SceneComposer2D`)
- Added backend adapter encoders for OpenGL, Vulkan, and DirectX command packets.
- Added Maven Central publishing metadata and signing flow (sources, javadocs, GPG signatures).
