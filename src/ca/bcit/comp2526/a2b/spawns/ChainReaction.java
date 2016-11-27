package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * ChainReaction.
 *
 * @author  Wei Zhou
 * @version 2016-11-27
 * @since   2016-11-14
 */
public class ChainReaction extends Spawn {

    /**
     * Creates a new ChainReaction.
     * @param world    that Lifeform will spawn in
     */
    public ChainReaction(final World world) {
        super(world);

        setUnspawnableTerrain(Terrain.WATER);

        addTerraformRate(Terrain.WATER, 0.1f);
        addTerraformRate(Terrain.LAND,  0.9f);

        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.CARNIVORE, 0.06f);
        addSpawnRate(LifeformType.HERBIVORE, 0.12f);

        addMortalityRate(LifeformType.PLANT,     0.00001f);
        addMortalityRate(LifeformType.CARNIVORE, 0.00001f);
        addMortalityRate(LifeformType.HERBIVORE, 0.15f);
    }
}
