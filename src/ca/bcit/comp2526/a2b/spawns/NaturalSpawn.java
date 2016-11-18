package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * NaturalSpawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-17
 * @since   2016-11-08
 */
public class NaturalSpawn extends Spawn {

    /**
     * Creates a new NaturalSpawn.
     * @param world    that Lifeform will spawn in
     */
    public NaturalSpawn(final World world) {
        super(world);

        setUnspawnableTerrain(Terrain.WATER);
        setConvergingTerrain(Terrain.WATER, 1.7f); // adds exponential growth to Terrain

        addTerraformRate(Terrain.WATER, 0.01f);
        addTerraformRate(Terrain.LAND,  0.99f);

        addSpawnRate(LifeformType.PLANT,     0.30f);
        addSpawnRate(LifeformType.HERBIVORE, 0.25f);
        addSpawnRate(LifeformType.CARNIVORE, 0.1f);
        addSpawnRate(LifeformType.OMNIVORE,  0.1f);

        addMortalityRate(LifeformType.PLANT,     0.01f);
        addMortalityRate(LifeformType.HERBIVORE, 0.2f);
        addMortalityRate(LifeformType.CARNIVORE, 0.00000001f);
        addMortalityRate(LifeformType.OMNIVORE,  0.055f);
    }
}
