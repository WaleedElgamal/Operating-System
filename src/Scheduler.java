package src;


import java.util.ArrayDeque;
import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private int runningProcessID;
    private int timeSlice; // represents the time slice for the round robin algorithm
    private int currentSlice;


    public Scheduler() {
        this.readyQueue = new ArrayDeque<>(); // ArrayDeque is faster than LinkedList when removing from middle of queue
        this.blockedQueue = new ArrayDeque<>();
        this.timeSlice = 0;
        this.currentSlice = 0;
        this.runningProcessID = -1;
    }

    public void schedule() {
        OperatingSystem operatingSystem = OperatingSystem.getInstance();
        if (runningProcessID != -1) {
            if (operatingSystem.processFinished(runningProcessID)) {
                terminateProcess(runningProcessID);
            }
            else if (currentSlice < timeSlice) {
                operatingSystem.execute(runningProcessID);
                currentSlice++;
                return;
//                schedule();
            } else {
                addToReadyQueue(runningProcessID);
                runningProcessID = -1;
                currentSlice = 0;
            }
        }
        if (!readyQueue.isEmpty()) {
            chooseNextProcess();
            if(!operatingSystem.processInMemory(runningProcessID)){
                operatingSystem.swapProcessToMemory(runningProcessID);
            }
            operatingSystem.execute(runningProcessID);
            currentSlice++;
            return;
//            schedule();
        }
    }

    public void chooseNextProcess() {
        currentSlice = 0;
        int pid = readyQueue.remove();
        runningProcessID = pid;
        OperatingSystem.getInstance().setProcessState(pid, State.RUNNING);
        printQueues();
    }

    public void addToReadyQueue(Integer pid) {
        readyQueue.add(pid);
        OperatingSystem.getInstance().setProcessState(pid, State.READY);
    }

    public void addToBlockedQueue(Integer pid) {
        blockedQueue.add(pid);
        OperatingSystem.getInstance().setProcessState(pid, State.BLOCKED);
    }

    public void terminateProcess(Integer pid) {
        OperatingSystem.getInstance().setProcessState(pid, State.FINISHED);
        printQueues();
    }

    // Resource management methods

    public void blockProcess(Integer pid) {
        readyQueue.remove(pid);
        addToBlockedQueue(pid);
        runningProcessID = -1;
        currentSlice = 0;
        printQueues();
    }

    public void unblockProcess(Integer pid) {
        blockedQueue.remove(pid);
        addToReadyQueue(pid);
    }


    public void printQueues(){
        System.out.println("Ready Queue: " + readyQueue);
        System.out.println("Blocked Queue: " + blockedQueue);
    }

    // Getters and setters

    public Queue<Integer> getReadyQueue() {
        return readyQueue;
    }

    public void setReadyQueue(Queue<Integer> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public Queue<Integer> getBlockedQueue() {
        return blockedQueue;
    }

    public void setBlockedQueue(Queue<Integer> blockedQueue) {
        this.blockedQueue = blockedQueue;
    }

    public int getRunningProcessID() {
        return runningProcessID;
    }

    public void setRunningProcessID(int runningProcessID) {
        this.runningProcessID = runningProcessID;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public int getCurrentSlice() {
        return currentSlice;
    }

    public void setCurrentSlice(int currentSlice) {
        this.currentSlice = currentSlice;
    }

    public void incrementCurrentSlice() {
        this.currentSlice++;
    }

}

