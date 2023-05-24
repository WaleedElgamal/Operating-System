package src;

public class Memory {


    private final int firstProgram = 15;
    private final int secondProgram = 27;
    private MemoryWord[] memory = new MemoryWord[40];


    // PCB order in memory  is: PID, State, ProgramCounter, MemoryBegin, MemoryEnd

    // PCBs

    // 0-4 -> PID 0 PCB
    // 5-9 -> PID 1 PCB
    // 10-14 -> PID 2 PCB

    // Processes in memory

    // 15-26 -> Process in location 1
    // 15-17 -> variables
    // 18-26 -> instructions

    // 27-38 -> Process in location 2
    // 27-29  -> variables
    // 30-38 -> instructions


    public MemoryWord[] getMemory() {
        return memory;
    }

    public void setMemory(MemoryWord[] memory) {
        this.memory = memory;
    }


    public int hasSpace() {  // returns -1 if no empty spaces
        if (memory[firstProgram] == null)// && taken[26]==false) //first slot of process is empty + maybe 26 wont be used
            return firstProgram;
        else if (memory[secondProgram] == null)// && taken[39]==false) //second slot of process is empty + maybe 39 wont be used
            return secondProgram;
        else
            return -1; // both slots are occupied
    }

    public int insertIntoMemoryCheck() { // returns -1 if no space and should be handled inside which one to remove, else returns position
        //if(hasSpace()>-1)
        return hasSpace();
        // else // need to check if there is space outside and checking which process to pre empt
    }

    public void insertInstructionsIntoMemory(int position, int pid, String program) { // position read from pcv attribute?
        // TODO handle program attribute in process for this logic to work
        String[] programSplit = program.split("\n");

        int instructionStart = position + 3; // 3 for the variables

        for (int memoryLoc = instructionStart; memoryLoc < (programSplit.length + instructionStart); memoryLoc++)
            memory[memoryLoc] = new MemoryWord("", programSplit[memoryLoc - instructionStart]); // memoryLoc - instructionStart to start from 0

        int pcbStart = pid * 5;
        memory[pcbStart + 3].setValue(position); // memory begin
        memory[pcbStart + 4].setValue(position + 11); // memory end
    }

    public void removeFromMemory(int pid) { //deletes process from memory by nullifying its segment
        int begin = getMemoryBegin(pid); // returns memory bound 1
        int end = getMemoryEnd(pid); // returns memory bound 2

        for (int i = begin; i <= end; i++) //nullifies position in memory
            memory[i] = null;


        // nullify memory bounds in PCB
        int pcbStart = pid * 5;
        memory[pcbStart + 3] = null; // nullify memory begin
        memory[pcbStart + 4] = null; // nullify memory end

    }


    // PCB info getters
    public State getProcessState(Integer pid) {
        // get state from memory
        int pcbStart = pid * 5;
        return (State) memory[pcbStart + 1].getValue();
    }

    public int getProgramCounter(Integer pid) {
        // get program counter from memory
        int pcbStart = pid * 5;
        return (int) memory[pcbStart + 2].getValue();
    }

    public int getMemoryBegin(Integer pid) {
        // get memory begin from memory
        int pcbStart = pid * 5;
        return (int) memory[pcbStart + 3].getValue();
    }

    public int getMemoryEnd(Integer pid) {
        // get memory end from memory
        int pcbStart = pid * 5;
        return (int) memory[pcbStart + 4].getValue();
    }
}
