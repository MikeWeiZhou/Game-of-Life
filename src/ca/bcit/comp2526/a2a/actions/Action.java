package ca.bcit.comp2526.a2a.actions;

import ca.bcit.comp2526.a2a.lifeforms.Lifeform;

import java.util.HashMap;
import java.util.Map;

/**
 * Action.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public abstract class Action {

    private static final String packageName;
    private static final Map<ActionType, Action> actions;

    static {
        packageName = "ca.bcit.comp2526.a2a.actions";
        actions     = new HashMap<ActionType, Action>();
        actions.put(ActionType.MOVE, createActionObj("Move"));
//        actions.put(ActionType.EAT,  createActionObj("Eat"));
    }

    /**
     * Act out action for Lifeform.
     */
    public abstract void act(Lifeform lf);

    /**
     * Take the specified Action and make LifeForm act it out.
     * @param at    ActionType
     * @param lf    Lifeform to perform the specified Action
     */
    public static void takeAction(final ActionType at, final Lifeform lf) {
        getActionObj(at).act(lf);
    }

    /*
     * Returns the Action object for the specified ActionType.
     */
    private static Action getActionObj(final ActionType at) {
        return actions.get(at);
    }

    /*
     * Create an Action object for the specified ActionType.
     */
    private static Action createActionObj(final String className) {
        try {
            return (Action) Class.forName(packageName + "." + className).newInstance();
        } catch (final ClassNotFoundException ex) {
            System.err.print("Cannot find class: " + className);
            System.exit(1);
        } catch (final InstantiationException ex) {
            System.err.println("Error creating: " + className);
            System.exit(1);
        } catch (final IllegalAccessException ex) {
            System.err.println(className + " must have a public, no-arg, constructor");
            System.exit(1);
        }

        // should be unreachable
        return (null);
    }
}
