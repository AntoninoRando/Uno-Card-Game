package events;

/**
 * Implemented by those model classes that wants to change the business
 * logic whenever the user asks for it.
 */
@FunctionalInterface
public interface InputListener {
    /**
     * Change this state based on the input given.
     * 
     * @param choice The input given by the user.
     */
    public void acceptInput(Object choice);
}