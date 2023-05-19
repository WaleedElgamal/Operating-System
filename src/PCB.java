package src;

public class PCB {
    private int pid;
    private State processState;
    private int programCounter;
    private int memoryBegin;
    private int memoryEnd;

    public PCB(int pid, State processState, int programCounter, int memoryBegin, int memoryEnd) {
        this.pid = pid;
        this.processState = processState;
        this.programCounter = programCounter;
        this.memoryBegin = memoryBegin;
        this.memoryEnd = memoryEnd;
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

    public int getMemoryBegin() {
        return memoryBegin;
    }
    public void setMemoryBegin(int memoryBegin) {
        this.memoryBegin = memoryBegin;
    }

    public int getMemoryEnd() {
        return memoryEnd;
    }
    public void setMemoryEnd(int memoryEnd) {
        this.memoryEnd = memoryEnd;
    }
}
