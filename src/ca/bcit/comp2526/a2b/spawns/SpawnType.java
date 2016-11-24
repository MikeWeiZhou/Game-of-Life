package ca.bcit.comp2526.a2b.spawns;

/**
 * SpawnType.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-19
 */
public enum SpawnType {

    /** GiantMess. */
    GIANT_MESS("Giant Mess"),

    /** ChainReaction. */
    CHAIN_REACTION("Chain Reaction");

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
