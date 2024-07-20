# ScalableLux
A Fabric mod based on Starlight that improves the performance of light updates in Minecraft.

## Why does this fork exist?
- Starlight is no longer maintained as a mod [since Mar 8, 2024](https://github.com/PaperMC/Starlight/commit/cca03d62da48e876ac79196bad16864e8a96bbeb).
- The performance of vanilla lighting engine is still a bottleneck for high-performance chunk generation.
- The base Starlight is still [100% faster than vanilla](<https://gist.github.com/Spottedleaf/6cc1acdd03a9b7ac34699bf5e8f1b85c#light-engine-performance-changes-in-120-from-vanilla>),
  allowing the chunk system to scale beyond 24 threads.
- Starlight's "stateless" design allows for parallel light updates, further widening the performance gap.
  It is still [rather important for dedicated servers with more players to stress chunk generation](https://gist.github.com/Spottedleaf/6cc1acdd03a9b7ac34699bf5e8f1b85c#is-starlight-obsolete).
  Therefore, it is still important for Fabric or other modded servers with plenty of players. 

## What does this fork do?
- Contains all the performance improvements from Starlight with additional bug fixes.
- Optionally allows for parallel light updates, bringing significant performance improvement in high-speed
  world generation and heavy light updates scenarios.

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
