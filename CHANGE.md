# CHANGELOG

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
