package src;

import java.util.Queue;

public class Mutex {
    private boolean locked = false;
    private int ownerID = -1;
    private Queue<Integer> blockedQueue;



    // need to change this to not use wait() and notify() and instead use semWait() and semSignal()

    // semWait is the same as lock
    public synchronized void lock(int processID) { // processID is the ID of the process that is trying to lock the mutex
        while (locked) { // while the mutex is locked,
            try {
                wait(); // wait until the mutex is unlocked
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ownerID = processID; // else, give the mutex to the process that is trying to lock it
        locked = true; // and set the mutex to be locked
    }


    // semSignal is the same as unlock
    public synchronized void unlock(int processID) { // processID is the ID of the process that is trying to unlock the mutex
        if (ownerID == -1) { // if the mutex is not locked,
            return; // then return
        }
        if (ownerID != processID) { // if the process that is trying to unlock the mutex is not the owner of the mutex,
            return; // then return
        }
        ownerID = -1; // else, unlock the mutex
        locked = false; // and set the mutex to be available
        notify(); // then notify all waiting processes
    }
}
