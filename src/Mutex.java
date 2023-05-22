package src;

import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    private boolean locked = false;
    private int ownerID = -1; // -1 means no owner
    private Queue<Integer> blockedQueue;

    public Mutex() {
        this.blockedQueue = new LinkedList<>();
    }

    public void semWait(int pid) {
        if (locked) {
            blockedQueue.add(pid);
        } else {
            locked = true;
            ownerID = pid;
        }
    }

    public void semSignal(int pid) {
        if(ownerID != pid) {
            return;
        }
        release();
    }

    private void release(){
        if (blockedQueue.isEmpty()) {
            locked = false;
            ownerID = -1;
        } else {
            ownerID = blockedQueue.remove();
        }
    }

    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public Queue<Integer> getBlockedQueue() {
        return blockedQueue;
    }
    public void setBlockedQueue(Queue<Integer> blockedQueue) {
        this.blockedQueue = blockedQueue;
    }
}