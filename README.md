> [!NOTE]  
> FiguraSVC has been archived as I do not plan on working on this broken codebase anymore. I plan on remaking this mod from scratch. You are free to fork and rewrite the mod if this doesn't happen.

<h1 align="center"> FiguraSVC</h1>
<p align="center">
  <img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg">
</p>


### FiguraSVC adds Simple Voice Chat intergation to Figura
Currently, documentation is kinda poor as im still working on a new release of this mod, check out the examples folder, it has a demo script taken from my avatar.

## How to build this repo
This fork is configured for the local setup and expects the Figura Fabric jar to be placed at:

- `libs/figura-0.1.5+1.21.8-fabric-mc.jar`

### How to build the Figura jar
1. Clone Figura from the `1.21.8` branch:
   - `https://github.com/FiguraMC/Figura/tree/1.21.8`
2. Build the Fabric jar from the Figura repo.
3. Copy the built jar into this repo at:
   - `FiguraSVC-compat/libs/figura-0.1.5+1.21.8-fabric-mc.jar`

### Build FiguraSVC-compat
1. Make sure the Figura jar is present at the path above.
2. Run the build with Java 21.
3. From `FiguraSVC-compat`, run `./gradlew clean build` or `gradlew build`.
4. The final remapped mod jar is produced in `build/libs/`.

If your system default Java is newer than 21, use a Java 21 runtime for Gradle when building this project.

Notes:
- Forge support has been removed from this vendored fork.
- This local setup is Fabric-only.
- `libs/` is gitignored for local jar dependencies.


[kofi]: https://img.shields.io/badge/Ko--fi-00b9fe?logo=kofi&logoColor=ffffff&labelColor=00b9fe

## Donate: [ ![kofi][] ](https://ko-fi.com/knownsh)
