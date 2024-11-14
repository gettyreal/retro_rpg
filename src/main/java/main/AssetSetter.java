package main;

import entity.*;
import entity.npc.*;
import map.GameMap;
import object.*;

public class AssetSetter {
    GamePanel gp;
    GameMap map;

    public AssetSetter(GamePanel gp, GameMap map) {
        this.gp = gp;
        this.map = map;
    }

    // adds obj into the game
    public void setObject(int mapIndex) {
        if (mapIndex == 0) {
            addObject(new OBJ_PokeChest(), 27, 23);
            addObject(new OBJ_Door(), 7, 9);
            addObject(new OBJ_PokeBall(), 5, 10);
            addObject(new OBJ_PokeBall(), 5, 19);
            addObject(new OBJ_PokeBall(), 10, 24);
            addObject(new OBJ_PokeBall(), 17, 30);
            addObject(new OBJ_PokeBall(), 37, 11);
            addObject(new OBJ_PokeBall(), 45, 42);
            addObject(new OBJ_PokeBall(), 20, 45);
        }
        if (mapIndex == 1) {
            addObject(new OBJ_Door(), 25, 27);
        }
    }

    // adds pokemon to the game.
    public void setPokemons(int mapIndex) {
        if (mapIndex == 0) {
            addPokemon(new Pokemon(gp, "cubone", "pokemon/cubone/cubone_", 8, 12), 21, 26);
            
        }
        if (mapIndex == 1) {
            addPokemon(new Pokemon(gp, "treecko", "pokemon/treecko/treecko_", 10, 12), 30, 25);
        }
    }

    public void setNPC(int mapIndex) {
        if (mapIndex == 0) {

        }
        if (mapIndex == 1) {
            addNPC(new Doctor_Oak(gp), 20, 21);
        }
    }

    // adds a new object into the game
    // specify into the superovject parameter which subclass to pass
    // example of usage = gp.obj.set(0, new OBJ_PokeChest());
    public void addObject(SuperObject obj, int tileColumm, int tileRow) {
        obj.worldX = tileColumm * gp.tileSize;
        obj.worldY = tileRow * gp.tileSize;
        map.obj.add(obj);
    }

    // removes a object from the game
    // specify the index of the object to remove.
    public void removeObject(int index) {
        if (index >= 0 && index < map.obj.size()) {
            map.obj.remove(index);
        }
    }

    // adds a new pokemon into the game
    // specify into the entity parameter which subclass pokemon to pass
    // example of usage = addPokemon(New Cubone(gp), 10, 10);
    public void addPokemon(Entity pokemonType, int tileColumm, int tileRow) {
        pokemonType.worldX = tileColumm * gp.tileSize;
        pokemonType.worldY = tileRow * gp.tileSize;
        map.pokemons.add(pokemonType);
    }

    // removes a pokemon entity from the game
    // specify the index of the pokemon to remove.
    public void removePokemon(int index) {
        if (index >= 0 && index < map.pokemons.size()) {
            map.pokemons.remove(index);
        }
    }

    public void addNPC(Entity npcType, int tileColumm, int tileRow) {
        npcType.worldX = tileColumm * gp.tileSize;
        npcType.worldY = tileRow * gp.tileSize;
        map.npc.add(npcType);
    }
}
