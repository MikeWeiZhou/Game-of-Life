package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;

/**
 * Animal.
 *
 * @author  Wei Zhou
 * @version 2016-11-18
 * @since   2016-11-08
 */
public abstract class Animal extends Lifeform {

    private Node targetLocation;

    /**
     * Constructs a new Animal.
     * @param lft     LifeformType
     * @param node    that contains this Animal
     * @param world   that contains this Lifeform
     */
    public Animal(final LifeformType lft, final Node node, final World world) {
        super(lft, node, world);
    }

    /**
     * Throws an error if Animal is in an illegal state.
     */
    @Override
    public void init() {
        if (getTargetTrait() == null) {
            throw new IllegalStateException("Failed to initialize Animal: must have a target "
                    + "Trait");
        }
        if (getMovement() == 0) {
            throw new IllegalStateException("Failed to initialize Animal: movement must be "
                    + "greater than 0");
        }

        super.init();
    }

    // ----------------------------------------- TAKE ACTION ---------------------------------------

    /**
     * Take action.
     */
    @Override
    public void takeAction() {
        findTarget();
        eat();
        move();

        super.takeAction();
    }

    /**
     * Finds and sets a suitable target Node that may contain food.
     */
    protected void findTarget() {
        final Node[] neighbors = getLocation().getNeighborsWithinWalkingDistanceFor(this);
        Node target = null;

        shuffleNodes(neighbors);

        for (Node n : neighbors) {
            if (n.getTerrain().equals(getInhabitable())) {
                continue;
            }

            if (!n.hasLivingInhabitant()) {
                if (target == null) {
                    target = n;
                } else if (n.getTerrain().equals(FOUNTAIN_OF_YOUTH)) {
                    target = n;
                }
            } else if (n.getInhabitant().hasTrait(getTargetTrait())) {
                target = n;
                break;
            }
        }

        setTargetLocation(target);
    }

    /**
     * Eats Lifeform at target Node, if edible.
     */
    protected void eat() {
        if (getTargetLocation() == null || !getTargetLocation().hasLivingInhabitant()) {
            return;
        }

        Lifeform lf = getTargetLocation().getInhabitant();
        if (lf.hasTrait(getTargetTrait())) {
            lf.kill();

            // +1 due to age() being called after eating
            // age() takes 1 health away
            setHealth(getMaxHealth() + 1);
        }
    }

    /**
     * Move to target Node if not null.
     */
    protected void move() {
        if (getTargetLocation() == null) {
            return;
        }

        moveTo(getTargetLocation());
    }

    /*
     * Shuffles the order of an array of Nodes.
     * @param nodes    to shuffle
     */
    private void shuffleNodes(final Node[] nodes) {
        for (int i = 1; i < nodes.length / 2; i++) {
            int rand    = getRandom().nextInt(nodes.length);
            Node tmp    = nodes[i];
            nodes[i]    = nodes[rand];
            nodes[rand] = tmp;
        }
    }

    // ----------------------------------------- SETTERS -------------------------------------------

    /**
     * Sets the target Node.
     * @param target    Node
     */
    protected void setTargetLocation(final Node target) {
        targetLocation = target;
    }

    // ----------------------------------------- GETTERS -------------------------------------------

    /**
     * Returns target Node.
     * @return target Node
     */
    protected Node getTargetLocation() {
        return targetLocation;
    }
}
