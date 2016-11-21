package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * GiantMess.
 *
 * @author  Wei Zhou
 * @version 2016-11-18
 * @since   2016-11-08
 */
public class GiantMess extends Spawn {

    /**
     * Creates a new GiantMess.
     * @param world    that Lifeform will spawn in
     */
    public GiantMess(final World world) {
        super(world);

        setUnspawnableTerrain(Terrain.WATER);

        addTerraformRate(Terrain.WATER, 0.1f);
        addTerraformRate(Terrain.LAND,  0.9f);

        addSpawnRate(LifeformType.PLANT,     0.30f);
        addSpawnRate(LifeformType.HERBIVORE, 0.22f);
        addSpawnRate(LifeformType.CARNIVORE, 0.1f);
        addSpawnRate(LifeformType.OMNIVORE,  0.1f);

        addMortalityRate(LifeformType.PLANT,     0.01f);
        addMortalityRate(LifeformType.HERBIVORE, 0.2f);
        addMortalityRate(LifeformType.CARNIVORE, 0.0000001f);
        addMortalityRate(LifeformType.OMNIVORE,  0.055f);
    }
}
