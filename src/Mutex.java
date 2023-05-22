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

    public void semWait(int pid, Scheduler scheduler) {
        if (locked) {
            blockedQueue.add(pid);
            scheduler.blockProcess(pid); // blockProcess will add the process to the blocked queue and remove it from ready queue + set its state to blocked
        } else {
            locked = true;
            ownerID = pid;
        }
    }

    public void semSignal(int pid, Scheduler scheduler) {
        if(ownerID != pid) {
            return;
        }
        release(pid, scheduler);
    }

    private void release(int pid, Scheduler scheduler){
        if (blockedQueue.isEmpty()) {
            locked = false;
            ownerID = -1;
        } else {
            ownerID = blockedQueue.remove();
            scheduler.unblockProcess(ownerID); // unblockProcess will add the process to the ready queue and remove it from the blocked queue + set its state to ready
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