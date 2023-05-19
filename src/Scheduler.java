package src;


import java.util.Queue;

public class Scheduler {
    private Queue<Integer> readyQueue;
    private Queue<Integer> blockedQueue;
    private MemoryWord[] memory = new MemoryWord[40];
    private int runningProcessID;
    private static Mutex inputMutex;
    private static Mutex outputMutex;
    private static Mutex fileMutex; // accessing a file on disk (read/write)
    private int timeSlice; // represents the time slice for the round robin algorithm
    private int[] processBegin; // starting position of process in memory
    private int[] processEnd; // ending position of process in memory
    private int[] currInstruction; // position of the current instruction relative to processBegin

    // need a variable for current instruction
    // array or arraylist to hold order of processes?
}

