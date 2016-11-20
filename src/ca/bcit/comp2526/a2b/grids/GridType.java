package ca.bcit.comp2526.a2b.grids;

/**
 * GridType.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-19
 */
public enum GridType {

    /** SquareGrid with SquareRenderer. */
    SQUARE("Square");

    private final String name;

    /**
     * Constructor.
     * @param name    of GridType.
     */
    GridType(final String name) {
        this.name = name;
    }

    /**
     * Returns name of GridType.
     * @return name of GridType.
     */
    public String getName() {
        return name;
    }
}
