package src;

public class PCB {
    private int pid;
    private State processState;
    private int programCounter;
    private int[] memoryBoundaries;

    public PCB(int pid, State processState, int programCounter, int[] memoryBoundaries) {
        this.pid = pid;
        this.processState = processState;
        this.programCounter = programCounter;
        this.memoryBoundaries = memoryBoundaries;
    }

    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }

    public State getProcessState() {
        return processState;
    }
    public void setProcessState(State processState) {
        this.processState = processState;
    }

    public int getProgramCounter() {
        return programCounter;
    }
    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int[] getMemoryBoundaries() {
        return memoryBoundaries;
    }
    public void setMemoryBoundaries(int[] memoryBoundaries) {
        this.memoryBoundaries = memoryBoundaries;
    }
}
