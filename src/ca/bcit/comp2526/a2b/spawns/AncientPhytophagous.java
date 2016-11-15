package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

/**
 * AncientPhytophagous.
 *
 * <p>
 *     Herbivores need a 50% mortality rate to stay in balance with the
 *     regeneration of Plants. Life is hard without meat.
 * </p>
 *
 * @author  Wei Zhou
 * @version 2016-11-14
 * @since   2016-11-14
 */
public class AncientPhytophagous extends Spawn {

    /**
     * Creates a new AncientPhytophagous.
     * @param world    that Lifeform will spawn in
     */
    public AncientPhytophagous(final World world) {
        super(world);

        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.HERBIVORE, 0.25f);

        addMortalityRate(LifeformType.PLANT,     0.01f);
        addMortalityRate(LifeformType.HERBIVORE, 0.5f);

        addTerraformRate(Terrain.WATER, 0.03f);
        addTerraformRate(Terrain.LAND,  0.97f);
    }
}
