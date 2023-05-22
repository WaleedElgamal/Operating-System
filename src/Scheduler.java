package src;


import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private MemoryWord[] memory = new MemoryWord[40];
    private int runningProcessID;
    private static Mutex inputMutex = new Mutex();
    private static Mutex outputMutex = new Mutex();
    private static Mutex fileMutex = new Mutex(); // accessing a file on disk (read/write)
    private int timeSlice; // represents the time slice for the round robin algorithm
    private int currentSlice;
    private int[] processBegin; // starting position of process in memory
    private int[] processEnd; // ending position of process in memory
    private int[] currInstruction; // position of the current instruction relative to processBegin

    // need a variable for current instruction
    // array or arraylist to hold order of processes?

    public Scheduler(int timeSlice) {
        this.readyQueue = new ArrayDeque<>(); // ArrayDeque is faster than LinkedList when removing from middle of queue
        this.blockedQueue = new ArrayDeque<>();
        this.timeSlice = timeSlice;
        this.processBegin = new int[10];
        this.processEnd = new int[10];
        this.currInstruction = new int[10];
        this.runningProcessID = -1;
        for (int i = 0; i < 40; i++) {
            memory[i] = new MemoryWord();
        }
    }

    public void schedule() {
        if (runningProcessID != -1) {
            if (currentSlice < timeSlice) {
                Main.execute(runningProcessID);
                currentSlice++;
            } else {
                runningProcessID = -1;
                currentSlice = 0;
                if (!readyQueue.isEmpty()) {
                    runningProcessID = readyQueue.remove();
                    Main.execute();
                    currentSlice++;
                }
                if () { //condition to check that we did not finish all the process instructions
                    readyQueue.add(runningProcessID);
                }

            }
        }
        else {
            if (!readyQueue.isEmpty()) {
                runningProcessID = readyQueue.remove();
                Main.execute();
                currentSlice++;
            }
        }
    }

    public void addToReadyQueue(int processID) {
        readyQueue.add(processID);
    }


    public void blockProcess(int pid) {
    readyQueue.remove(pid);
    blockedQueue.add(pid);
    // set process state to blocked
    }
    public void unblockProcess(int pid) {
        blockedQueue.remove(pid);
        readyQueue.add(pid);
        // set process state to ready
    }

}

