# Lattice2D (Java)

Toolkit helper for 2D mesh/deformer/clip/draw-order orchestration.
This project intentionally does not render by itself; your app controls GPU rendering.

## Maven setup (Windows)

Use Maven from `C:\.Apache\apache-maven-3.9.11`.

Temporary for current terminal:

```powershell
$env:MAVEN_HOME = 'C:\.Apache\apache-maven-3.9.11'
$env:PATH = "$env:MAVEN_HOME\\bin;$env:PATH"
mvn -version
```

Persist for your user account:

```powershell
setx MAVEN_HOME "C:\.Apache\apache-maven-3.9.11"
setx PATH "$([Environment]::GetEnvironmentVariable('PATH','User'));C:\.Apache\apache-maven-3.9.11\\bin"
```

Build all modules with explicit Maven path:

```powershell
& "C:\.Apache\apache-maven-3.9.11\\bin\\mvn.cmd" -q clean package
```

## Modules

- `core`: data model and composition pipeline (`Texture + Mesh + Deformer + ClippingMask + DrawOrder`)
- `backend-opengl`: helper encoder for OpenGL-facing command packets
- `backend-vulkan`: helper encoder for Vulkan-facing command packets
- `backend-dx`: helper encoder for DirectX-facing command packets

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
& "C:\.Apache\apache-maven-3.9.11\bin\mvn.cmd" -q -pl backend-dx -Pwith-directjlib -Ddirectjlib.version=1.0.0 clean package
```

Enable GLFW profile for any backend module:

```powershell
& "C:\.Apache\apache-maven-3.9.11\bin\mvn.cmd" -q -pl backend-opengl -Pwith-glfw clean package
```

If you have a local `directjlib.jar`, install it once to local Maven repo:

```powershell
& "C:\.Apache\apache-maven-3.9.11\bin\mvn.cmd" install:install-file `
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
