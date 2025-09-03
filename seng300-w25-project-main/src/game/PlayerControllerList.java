package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A list for storing and iterating through player controllers.
 */
public class PlayerControllerList extends ArrayList<PlayerController> implements Serializable {
    private final int maxPlayers;
    private int currentIndex = 0;

    public PlayerControllerList(int initialCapacity) {
        super(initialCapacity);
        this.maxPlayers = initialCapacity;
    }

    /**
     * Deep copy constructor.
     * @param other object to deep copy
     */
    public PlayerControllerList(PlayerControllerList other) {
        super(other.getMaxPlayers());
        this.maxPlayers = other.getMaxPlayers();
        this.currentIndex = other.currentIndex;
        for (int i = 0; i < other.getMaxPlayers(); i++) {
            this.add(new PlayerController(other.get(i)));
        }
    }

    public PlayerController current() {
        return this.get(currentIndex);
    }

    public PlayerController next() {
        currentIndex = (currentIndex + 1) % this.size();
        return this.get(currentIndex);
    }

    public PlayerController previous() {
        currentIndex = (currentIndex - 1) % this.size();
        return this.get(currentIndex);
    }

    public void skip() {
        currentIndex = (currentIndex + 1) % this.size();
    }

    public boolean isFull() {
        return (size() == maxPlayers);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public boolean add(PlayerController player) {
        if (contains(player)) {
            throw new IllegalArgumentException("Player is already in the list");
        } else if (isFull()) {
            throw new IllegalArgumentException("Player list is already full");
        }
        return super.add(player);
    }
}
