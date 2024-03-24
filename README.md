# ScalableLux
A Fabric mod based on Starlight that improves the performance of light updates in Minecraft.
This is done by taking advantage of multiple CPU cores in parallel.

## Why does this fork exist?
- Starlight is no longer maintained as a mod [since Mar 8, 2024](https://github.com/PaperMC/Starlight/commit/cca03d62da48e876ac79196bad16864e8a96bbeb).
- The performance of vanilla lighting engine is still a bottleneck for high-performance chunk generation.
- Starlight's "stateless" design allows for parallel light updates.
  This is critical for allowing the chunk system to scale beyond 25 worker threads.

## Building and setting up

#### Initial setup
Run the following commands in the root directory:

```
git submodule update --init
./build.sh up
./build.sh patch
```

#### Creating a patch
See [CONTRIBUTING.md](CONTRIBUTING.md) for more detailed information.


#### Compiling
Use the command `./build.sh build`. Compiled jars will be placed under `Starlight-Patched/build/libs/`.
