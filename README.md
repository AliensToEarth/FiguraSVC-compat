# FiguraSVC-compat

**Simple Voice Chat integration for Figura** — now actually builds and runs on modern Fabric.

Your avatar can finally hear the world (and change how it sounds) instead of just looking pretty in silence.

### What it does
- Real-time voice processing via Simple Voice Chat
- Lua API for voice effects, lip-sync, visualization, and voice changing
- Full host microphone support
- Flashback export compatibility

### What’s new in this fork
- Updated for Figura 0.1.5+1.21.8 (Fabric only — I kept it simple)
- Slightly updated internal voice data format (see note below)
- Cleaner code, better Lua integration

Documentation is still “in progress”.

### Voice Format Note
This fork uses a slightly updated (and extended) voice data format for new features like the improved voice API and Flashback support. Full backwards compatibility with the original format is already built in — legacy avatars work exactly as before.
## How to build this repo
This fork is configured for the local setup and expects the Figura Fabric jar to be placed at:

- `libs/figura-0.1.5+1.21.8-fabric-mc.jar`

### How to build the Figura jar
1. Download Figura from the `1.21.8` branch:
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

Original project by KnownSH (thank you!).  
This is a compatibility fork by me, because archived repos make me sad.
