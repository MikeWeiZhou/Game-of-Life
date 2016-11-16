package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * OmnivoresOnly.
 *
 * @author  Wei Zhou
 * @version 2016-11-14
 * @since   2016-11-14
 */
public class OmnivoresOnly extends Spawn {

    /**
     * Creates a new OmnivoresOnly.
     * @param world    that Lifeform will spawn in
     */
    public OmnivoresOnly(final World world) {
        super(world);

        addSpawnRate(LifeformType.PLANT,    0.3f);
        addSpawnRate(LifeformType.OMNIVORE, 0.1f);

        addMortalityRate(LifeformType.PLANT,    0.01f);
        addMortalityRate(LifeformType.OMNIVORE, 0.06f);

        addTerraformRate(Terrain.WATER, 0.03f);
        addTerraformRate(Terrain.LAND,  0.97f);
    }
}
