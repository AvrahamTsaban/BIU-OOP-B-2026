package managers;

import Listeners.HitListener;

/**
 * The HitNotifier interface defines the contract for objects that want to notify HitListeners when a hit event occurs.
 */
public interface HitNotifier {
    /**
     * Add hl as a listener to hit events.
     * @param hl the HitListener to add as a listener to hit events
     */
    void addHitListener(HitListener hl);

    /**
     * Remove hl from the list of listeners to hit events.
     * @param hl the HitListener to remove from the list of listeners to hit events
     */
    void removeHitListener(HitListener hl);
}