import java.util.ArrayList;

public class GameEnvironment {
    ArrayList<Collidable> collidables;

    /**
     * Create a new game environment, ready to hold collidable objects.
     */
    public GameEnvironment() {
        collidables = new ArrayList<Collidable>();
    }

    /**
     * Add the given collidable to the environment.
     * @param c the collidable to add
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    // Assume an object moving from line.start() to line.end().
    // If this object will not collide with any of the collidables
    // in this collection, return null. Else, return the information
    // about the closest collision that is going to occur.
    public CollisionInfo getClosestCollision(Line trajectory) {
        //TODO: adjust Line class to not touch directions, and then implement this method.
        return null;
    }

}