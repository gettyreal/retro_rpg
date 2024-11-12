package main;

import entity.Entity;
import entity.pokemon.Cubone;
import object.OBJ_Door;
import object.OBJ_PokeBall;
import object.OBJ_PokeChest;
import object.SuperObject;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    //adds obj into the game
    public void setObject() {
        addObject(new OBJ_PokeChest(), 27, 23);
        addObject(new OBJ_Door(), 24, 22);
        addObject(new OBJ_PokeBall(), 5, 10);
        addObject(new OBJ_PokeBall(), 5, 19);
        addObject(new OBJ_PokeBall(), 10, 24);
        addObject(new OBJ_PokeBall(), 17, 30);
        addObject(new OBJ_PokeBall(), 37, 11);
        addObject(new OBJ_PokeBall(), 45, 42);
        addObject(new OBJ_PokeBall(), 20, 45);
    }

    //adds a new object into the game
    //specify into the superovject parameter which subclass to pass 
    //example of usage = gp.obj.set(0, new OBJ_PokeChest());
    public void addObject(SuperObject obj, int tileColumm, int tileRow) {
        obj.worldX = tileColumm * gp.tileSize;
        obj.worldY = tileRow * gp.tileSize;
        gp.obj.add(obj);
    }

    //removes a object from the game
    //specify the index of the object to remove.
    public void removeObject(int index) {
        if (index >= 0 && index < gp.obj.size()) {
            gp.obj.remove(index);
        }
    }

    //adds pokemon to the game.
    public void setPokemons() {
        addPokemon(new Cubone(gp), 21, 26);
    }

    //adds a new pokemon into the game
    //specify into the entity parameter which subclass pokemon to pass 
    //example of usage = addPokemon(New Cubone(gp), 10, 10);
    public void addPokemon(Entity pokemonType, int tileColumm, int tileRow) {
        pokemonType.worldX = tileColumm * gp.tileSize;
        pokemonType.worldY = tileRow * gp.tileSize;
        gp.pokemons.add(pokemonType);
    }

    //removes a pokemon entity from the game
    //specify the index of the pokemon to remove.
    public void removePokemon(int index) {
        if (index >= 0 && index < gp.pokemons.size()) {
            gp.pokemons.remove(index);
        }
    }
}
