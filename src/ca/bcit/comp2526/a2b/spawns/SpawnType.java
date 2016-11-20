package ca.bcit.comp2526.a2b.spawns;

/**
 * SpawnType.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-19
 */
public enum SpawnType {

    /** NaturalSpawn. */
    NATURAL_SPAWN("Natural"),

    /** OmnivoresOnly. */
    OMNIVORES_ONLY("Omnivores"),

    /** AllIDoIsEat. */
    ALL_I_DO_IS_EAT("All I Do Is Eat");

    private final String name;

    /*
     * Constructor.
     * @param name    of SpawnType
     */
    SpawnType(final String name) {
        this.name = name;
    }

    /**
     * Returns name of SpawnType.
     * @return name of SpawnType
     */
    public String getName() {
        return name;
    }
}
