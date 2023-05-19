package src;

import java.util.Queue;

public class Mutex {
    private boolean locked = false;
    private int ownerID = -1; // -1 means no owner
    private Queue<Integer> blockedQueue;




}
