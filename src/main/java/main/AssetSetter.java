package main;

import entity.*;
//import entity.npc.*;
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
            addObject(new OBJ_Door("toBirchLab", gp, "door2", 1), 22, 56);
            addObject(new OBJ_Door("toPlayerHouse", gp, "door1", 1), 19, 48);
            addObject(new OBJ_Sign("LITTLEROOT TOWN", gp.userInterface), 29, 52);
            addObject(new OBJ_Sign( gp.player.name +"'S HOUSE", gp.userInterface), 23, 48);
            addObject(new OBJ_Sign("BIRCH'S HOUSE", gp.userInterface), 28, 48);
        }
        if (mapIndex == 1) {
            addObject(new OBJ_Door("fromBirchLab", gp, (OBJ_Door)gp.mapM.maps.get(0).obj.get(0)), 7, 13);
        }
        if (mapIndex == 2) {
            addObject(new OBJ_Door("fromPlayerHouse", gp, (OBJ_Door)gp.mapM.maps.get(0).obj.get(1)), 4, 9);
            addObject(new OBJ_Stairs("toSecondFloor"), 11, 2);
            addObject(new OBJ_Stairs("fromSecondFloor"), 36, 2);
        }
        
    }

    // adds pokemon to the game.
    public void setPokemons(int mapIndex) {
        /* 
        if (mapIndex == 0) {   
            addPokemon(new Pokemon(gp, "treecko", "treecko/treecko_", "treecko/treecko_battle.png", 10, 12), 21, 26);
        }
        if (mapIndex == 1) {
            addPokemon(new Pokemon(gp, "treecko", "treecko/treecko_", "treecko/treecko_battle.png", 10, 12), 30, 25);
        }
        if (mapIndex == 2) {
        }*/
    }

    public void setNPC(int mapIndex) {
        if (mapIndex == 0) {
            addNPC(new NPC(gp, "npc15", "NPC 15 walk"), 30, 55);
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
