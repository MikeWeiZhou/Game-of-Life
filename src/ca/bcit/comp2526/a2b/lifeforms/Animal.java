package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

/**
 * Animal.
 *
 * @author  Wei Zhou
 * @version 2016-11-11
 * @since   2016-11-08
 */
public abstract class Animal extends Lifeform {

    private Node target;

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
        super.init();
    }

    /**
     * Take action.
     */
    @Override
    public void takeAction() {
        findTarget();
        eat();
        move();

        target = null;
        super.takeAction();
    }

    /*
     * Find edible target Node, or Node that is in the same direction.
     * If no available target, sets target = null
     */
    private void findTarget() {
        final Node[] targets = getNode().getNeighborsForLevel(getVisionLevel());
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
    }

    /*
     * Eat Lifeform at target Node, if exists and edible.
     */
    private void eat() {
        final Lifeform targetLF = getWorld().getLifeformAt(target);
        if (targetLF != null && targetLF.isAlive() && targetLF.hasTrait(getTargetTrait())) {
            targetLF.kill();

            // +1 due to eat first then move in logic
            // suppose to move in first, then eat
            setHealth(getMaxHealth() + 1);
        }
    }

    /*
     * Move to target Node
     */
    private void move() {
        if (target != null) {
            setNode(target);
        }
    }
}
