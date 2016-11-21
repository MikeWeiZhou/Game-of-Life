package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * AllIDoIsEat.
 *
 * @author  Wei Zhou
 * @version 2016-11-16
 * @since   2016-11-14
 */
public class AllIDoIsEat extends Spawn {

    /**
     * Creates a new AllIDoIsEat.
     * @param world    that Lifeform will spawn in
     */
    public AllIDoIsEat(final World world) {
        super(world);

        setUnspawnableTerrain(Terrain.WATER);

        addTerraformRate(Terrain.WATER, 0.1f);
        addTerraformRate(Terrain.LAND,  0.9f);

        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.HERBIVORE, 0.25f);

        addMortalityRate(LifeformType.PLANT,     0.01f);
        addMortalityRate(LifeformType.HERBIVORE, 0.17f);
    }
}
