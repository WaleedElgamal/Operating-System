package src;


import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private MemoryWord[] memory;
    private int runningProcessID;
    private Mutex inputMutex;
    private Mutex outputMutex;
    private Mutex diskMutex; // accessing a file on disk
    private int timeSlice; // represents the time slice for the round robin algorithm

    // need a variable for current instruction
}
