# Changelog

## 1.2.4 - 2026-10-12

- Added three options to the common config (`meteorites` section, all `false` by default):
  - `disableCertusQuartz`: the certus quartz blocks inside meteorites are replaced with sky stone.
  - `disableBuddingQuartz`: the budding certus quartz inside meteorites is replaced with sky stone and no quartz buds grow on it.
  - `disableMysteriousCube`: meteorites generate without the mysterious cube.

## 1.2.3 - 2026-09-29

- Meteorites now sit on top of the ground instead of staying buried when no crater is generated.
- Crater lakes are disabled so no water floods the terrain around the meteorite.
- Trees and huge mushrooms covering the meteorite are removed whole (trunk and their own leaves), plus snow, vines and cocoa left without support.
- Fixed mods.toml dependencies for Forge 1.20.1, which made the mod fail to load.
- The mixin config is now registered in the jar manifest, so the mixins actually apply on Forge 1.20.1.
- Added pack.mcmeta, required by Forge 1.20.1.
