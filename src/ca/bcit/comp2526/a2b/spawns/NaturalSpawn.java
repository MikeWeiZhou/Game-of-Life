package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * NaturalSpawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-14
 * @since   2016-11-08
 */
public class NaturalSpawn extends Spawn {

    /**
     * Creates a new NaturalSpawn.
     * @param world    that Lifeform will spawn in
     */
    public NaturalSpawn(final World world) {
        super(world);

        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.HERBIVORE, 0.15f);
        addSpawnRate(LifeformType.CARNIVORE, 0.1f);
        addSpawnRate(LifeformType.OMNIVORE,  0.1f);

        addMortalityRate(LifeformType.PLANT,     0.01f);
        addMortalityRate(LifeformType.HERBIVORE, 0.3f);
        addMortalityRate(LifeformType.CARNIVORE, 0.2f);
        addMortalityRate(LifeformType.OMNIVORE,  0.06f);

        addTerraformRate(Terrain.WATER, 0.03f);
        addTerraformRate(Terrain.LAND,  0.97f);
    }
}
