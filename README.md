# FiguraSVC-compat

**Simple Voice Chat integration for Figura** — now actually builds and runs on modern Fabric.

### What it does
- Real-time voice processing via Simple Voice Chat
- Lua API for lip-sync, visualization
- Flashback export compatibility

### What’s new in this fork
- Updated for Figura 0.1.5+1.21.11 (Fabric only — I kept it simple)
- Cleaner code, better Lua integration
- Older compatibility paths preserved  

Documentation is still “in progress”.


## How to build this repo
This fork is configured for the local setup and expects the Figura Fabric jar to be placed at:

- `deps/figura-0.1.5+1.21.11-fabric-mc.jar`

### How to build the Figura jar
1. Download Figura from the `1.21.11` branch:
   - `https://github.com/FiguraMC/Figura/tree/1.21.11`
2. Build the Fabric jar from the Figura repo (git checkout "1.21.11" && ./gradlew :fabric:build).
3. Copy the built jar into this repo at:
   - `FiguraSVC-compat/deps/figura-0.1.5+1.21.11-fabric-mc.jar`

### Build FiguraSVC-compat
1. Make sure the Figura jar is present at the path above.
2. Build with Java 21.
3. From `FiguraSVC-compat`, run `./gradlew clean build` or `gradle build`.
4. The final remapped mod jar is produced in `build/libs/`.

If your system default Java is newer than 21, use a Java 21 runtime for Gradle when building this project.

Notes:
- Forge support has been removed from this vendored fork.
- This local setup is Fabric-only.
- `deps/` is gitignored for local jar dependencies.

Original project by KnownSH (thank you!).  
This is a compatibility fork by me, because archived repos make me sad.
