# EMIDiscovery

![EMIDiscovery Logo](https://github.com/murphy-slaw/emi-discovery/blob/1.20.1/fabric/src/main/resources/icon_large.png?raw=true)

Prevents EMI from giving away all the secrets in your modpack by hiding items until the player has held one in their 
inventory.

## How it works
- Only items that the player has held appear in the index.
- The craftable/recipe view only shows recipes that can be made entirely from known items.
- Craftable recipes show their output item, but you can't do use or recipe lookups on them until they've been crafted.
- Tag and list ingredients are filtered to hide unknown items in the set.
- Known items can be prepopulated from a JSON file.
- Known items are stored client side, separately for each player and world.
- Works with EMI++ Backport item groups.

## Configuration

Currently the only configuration is the prepopulated items file, `config/emi_discovery_pre_discovered.json`. The 
format is a JSON array of strings specifying items by registry ID. Example:
```
[
    "minecraft:stone",
    "minecraft:iron_ingot"
]
```
The contents are copied to the the known items list of any player who doesn't already have one at login.

## Acknowledgements
- Inspired by, based on, and reuses code from [Discovered Enough Items](https://www.curseforge.com/minecraft/mc-mods/dei)
- Icon uses [Archaeologist by Jevgenijs Kudinovs](https://thenounproject.com/icon/archaeologist-7507904/) from [The 
  Noun Project](https://thenounproject.com)
