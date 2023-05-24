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

    public void semWait(Integer pid, Scheduler scheduler) {
        // If the lock is already acquired by another process
        if (locked) {
            // Add the current process to the blocked queue
            blockedQueue.add(pid);
            // Block the current process using the scheduler
            scheduler.blockProcess(pid);
        } else {
            // Acquire the lock for the current process
            locked = true;
            ownerID = pid;
        }
    }

    public void semSignal(Integer pid, Scheduler scheduler) {
        // If the current process is the owner of the lock
        if (ownerID == pid) {
            // Release the lock
            release(scheduler);
        }
    }

    private void release(Scheduler scheduler) {
        // If no other process is waiting for the lock
        if (blockedQueue.isEmpty()) {
            // Release the lock
            locked = false;
            ownerID = -1;
        } else {
            // Assign the lock to the next process in the blocked queue
            Integer currProcess = blockedQueue.remove();
            ownerID = currProcess;
            // Unblock the next process using the scheduler
            scheduler.unblockProcess(currProcess);
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