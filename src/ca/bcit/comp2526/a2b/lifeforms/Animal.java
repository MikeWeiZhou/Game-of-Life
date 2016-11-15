package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

/**
 * Animal.
 *
 * @author  Wei Zhou
 * @version 2016-11-14
 * @since   2016-11-08
 */
public abstract class Animal extends Lifeform {

    private Node targetNode;

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
        if (getVisionLevel() == 0 || getTargetTrait() == null) {
            throw new IllegalStateException();
        }

        super.init();
    }

    /**
     * Take action.
     */
    @Override
    public void takeAction() {
        targetNode = findTarget();
        eat(targetNode);
        moveTo(targetNode);

        targetNode = null;
        super.takeAction();
    }

    /**
     * Finds suitable target Node to eat and/or moveTo.
     */
    protected Node findTarget() {
        final Node[] targets = getNode().getNeighborsForLevel(getVisionLevel());
              Node   target  = null;

        for (Node n : targets) {
            if (n.getTerrain() == getInhabitable()) {
                continue;
            }

            Lifeform lf = getWorld().getLifeformAt(n);
            if (lf == null) {
                if (target == null) {
                    target = n;
                } else if (n.getTerrain() == Terrain.WATER) {
                    target = n;
                }
            } else if (lf.hasTrait(getTargetTrait())) {
                target = n;
                break;
            }
        }

        return target;
    }

    /**
     * Eats Lifeform at target Node, if edible.
     * @param target    Node
     */
    protected void eat(final Node target) {
        final Lifeform targetLF = getWorld().getLifeformAt(target);
        if (targetLF != null && targetLF.isAlive() && targetLF.hasTrait(getTargetTrait())) {
            targetLF.kill();

            // +1 due to eat first then moveTo in logic
            // suppose to moveTo in first, then eat
            setHealth(getMaxHealth() + 1);
        }
    }

    /**
     * Move to target Node if not null.
     * @param target    Node
     */
    protected void moveTo(final Node target) {
        if (target != null) {
            setNode(target);
        }
    }
}
