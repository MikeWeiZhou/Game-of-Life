package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

import java.util.Random;

/**
 * Animal.
 *
 * @author  Wei Zhou
 * @version 2016-11-15
 * @since   2016-11-08
 */
public abstract class Animal extends Lifeform {

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
        if (getMovement() == 0 || getTargetTrait() == null) {
            throw new IllegalStateException();
        }

        super.init();
    }

    /**
     * Take action.
     */
    @Override
    public void takeAction() {
        Node target;

        target = findTarget();
        eatAt(target);
        moveTo(target);

        super.takeAction();
    }

    /**
     * Finds and returns a suitable target Node to eatAt and/or move.
     * @return a suitable target Node to eatAt/move
     */
    protected Node findTarget() {
        final Node[] targets = getNode().getNeighborsFor(this);
              Node   target  = null;

        shuffleNodes(targets);

        for (Node n : targets) {
            if (n.getTerrain() == getInhabitable()) {
                continue;
            }

            Lifeform lf = getWorld().getLifeformAt(n);
            if (lf == null) {
                if (target == null) {
                    target = n;
                } else if (n.getTerrain().equals(Terrain.WATER)) {
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
    protected void eatAt(final Node target) {
        if (target == null) {
            return;
        }

        final Lifeform lf = getWorld().getLifeformAt(target);
        if (lf != null && lf.hasTrait(getTargetTrait())) {
            lf.kill();

            // +1 due to age() being called after eating
            // age() takes 1 health away
            setHealth(getMaxHealth() + 1);
        }
    }

    /**
     * Move to target Node if not null.
     * @param target    Node
     */
    protected void moveTo(final Node target) {
        if (target == null) {
            return;
        }

        setNode(target);
    }

    /*
     * Shuffles the order of an array of Nodes.
     * @param nodes    to shuffle
     */
    private void shuffleNodes(final Node[] nodes) {
        for (int i = 1; i < nodes.length / 2; i++) {
            int rand    = getWorld().getRandom().nextInt(nodes.length);
            Node tmp    = nodes[i];
            nodes[i]    = nodes[rand];
            nodes[rand] = tmp;
        }
    }
}
