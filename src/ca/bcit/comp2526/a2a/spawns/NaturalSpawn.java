package ca.bcit.comp2526.a2a.spawns;

import ca.bcit.comp2526.a2a.lifeforms.LifeformType;

/**
 * NaturalSpawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-08
 */
public class NaturalSpawn extends Spawn {

    public NaturalSpawn() {
        addSpawnRate(LifeformType.LIFELESS,  0.6f);
        addSpawnRate(LifeformType.PLANT,     0.3f);
        addSpawnRate(LifeformType.HERBIVORE, 0.1f);
    }
}
