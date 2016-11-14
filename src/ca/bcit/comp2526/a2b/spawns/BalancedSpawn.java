package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * BalancedSpawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-13
 */
public class BalancedSpawn extends Spawn {

    /**
     * Creates a new BalancedSpawn.
     * @param world    that Lifeform will spawn in
     */
    public BalancedSpawn(final World world) {
        super(world);

        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.HERBIVORE, 0.1f);
        addSpawnRate(LifeformType.CARNIVORE, 0.04f);
        addSpawnRate(LifeformType.OMNIVORE,  0.1f);

        addTerraformRate(Terrain.WATER, 0.03f);
        addTerraformRate(Terrain.LAND,  0.97f);
    }
}
