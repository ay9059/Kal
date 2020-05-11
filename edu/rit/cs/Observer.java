/*
 * Observer.java
 * PROVIDED file for the Kal GUI lab in csci142.
 */

package edu.rit.cs;

/**
 * An interface representing any class whose objects get notified when the objects they are observing update
 * themselves.
 *
 * @param <Subject> the type of object an implementor of this interface is observing
 * @author RIT CS
 */
public interface Observer< Subject > {

    /**
     * The observed subject calls this method on each observer that has
     * previously registered with it. This version of the design pattern
     * follows the "push model" in that typically the observer must then
     * query the subject parameter to find out what has happened.
     * Therefore it is often the case that the observed subject calls
     * this method with an argument value of <code>this</code>, but
     * this convention is by no means a requirement. Similarly, if this
     * is a simple signal with no data attached, or if it can safely
     * be assumed that the observer already has a reference to the subject,
     * the subject parameter may be null.
     * But as always this would have to be agreed to by both sides.
     *
     * @param subject object that informs this object about something.
     * @param something object that represents new information about subject
     *
     * @see <a href="https://sourcemaking.com/design_patterns/observer">
     *       the Observer design pattern</a>
     */
    void update( Subject subject, Object something );

}
