package src;


import java.util.ArrayDeque;
import java.util.Queue;

public class Scheduler {
    private static Mutex inputMutex = new Mutex();
    private static Mutex outputMutex = new Mutex();
    private static Mutex fileMutex = new Mutex(); // accessing a file on disk (read/write)
    private static Memory memory = new Memory();
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private int runningProcessID;
    private int timeSlice; // represents the time slice for the round robin algorithm
    private int currentSlice;
    private Object[] tempValue;


    public Scheduler(int timeSlice) {
        this.readyQueue = new ArrayDeque<>(); // ArrayDeque is faster than LinkedList when removing from middle of queue
        this.blockedQueue = new ArrayDeque<>();
        this.timeSlice = timeSlice;
        this.runningProcessID = -1;
        // TODO: create the memory
    }

    public static Mutex getInputMutex() {
        return inputMutex;
    }

    public static void setInputMutex(Mutex inputMutex) {
        Scheduler.inputMutex = inputMutex;
    }

    public static Mutex getOutputMutex() {
        return outputMutex;
    }

    public static void setOutputMutex(Mutex outputMutex) {
        Scheduler.outputMutex = outputMutex;
    }

    // Getters and setters

    public static Mutex getFileMutex() {
        return fileMutex;
    }

    public static void setFileMutex(Mutex fileMutex) {
        Scheduler.fileMutex = fileMutex;
    }

    public static Memory getMemory() {
        return memory;
    }

    public static void setMemory(Memory memory) {
        Scheduler.memory = memory;
    }

    public void schedule() {
        if (runningProcessID != -1) {
            if (currentSlice < timeSlice) {
                Main.execute(runningProcessID); //still not implemeneted
                currentSlice++;
            } else {
                runningProcessID = -1;
                currentSlice = 0;
                if (!readyQueue.isEmpty()) {
                    runningProcessID = readyQueue.remove();
                    Main.execute();
                    currentSlice++;
                }
                if () { //condition to check that we did not finish all the pid instructions
                    readyQueue.add(runningProcessID);
                }

            }
        } else {
            if (!readyQueue.isEmpty()) {
                runningProcessID = readyQueue.remove();
                Main.execute();
                currentSlice++;
            }
        }
    }

    public void addToReadyQueue(Integer pid) {
        readyQueue.add(pid);
    }

    // Resource management methods

    public void blockProcess(Integer pid) {
        readyQueue.remove(pid);
        blockedQueue.add(pid);
        // TODO: set process state to Blocked
    }

    public void unblockProcess(Integer pid) {
        blockedQueue.remove(pid);
        readyQueue.add(pid);
        // TODO: set process state to ready/running based on process
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

    public Object[] getTempValue() {
        return tempValue;
    }

    public void setTempValue(Object[] tempValue) {
        this.tempValue = tempValue;
    }
}

