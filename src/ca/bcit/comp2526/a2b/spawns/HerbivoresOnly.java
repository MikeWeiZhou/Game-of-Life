package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * HerbivoresOnly.
 *
 * <p>
 *     Herbivores need a 50% mortality rate to stay in balance with the
 *     regeneration of Plants. Life is hard without meat.
 * </p>
 *
 * @author  Wei Zhou
 * @version 2016-11-16
 * @since   2016-11-14
 */
public class HerbivoresOnly extends Spawn {

    /**
     * Creates a new HerbivoresOnly.
     * @param world    that Lifeform will spawn in
     */
    public HerbivoresOnly(final World world) {
        super(world);

        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.HERBIVORE, 0.25f);

        addMortalityRate(LifeformType.PLANT,     0.01f);
        addMortalityRate(LifeformType.HERBIVORE, 0.33f);

        addTerraformRate(Terrain.WATER, 0.03f);
        addTerraformRate(Terrain.LAND,  0.97f);
    }
}
