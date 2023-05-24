package src;

import java.io.*;

public class OperatingSystem {

    private static Mutex inputMutex;
    private static Mutex outputMutex;
    private static Mutex fileMutex; // accessing a file on disk (read/write)
    private static MemoryWord[] memory;
    // locations of first and second loaded programs in memory
    private final Integer firstProgram = 15;
    private final Integer secondProgram = 27;
    private Scheduler scheduler;
    // PCB order in memory  is: PID, State, ProgramCounter, MemoryBegin, MemoryEnd

    // PCBs

    // 0-4 -> PID 0 PCB
    // 5-9 -> PID 1 PCB
    // 10-14 -> PID 2 PCB

    // Processes in memory

    // 15-26 -> Process in location 1
    // 15-23 -> instructions
    // 24-26 -> variables

    // 27-38 -> Process in location 2
    // 27-35  -> instructions
    // 36-38 -> variables

    public OperatingSystem(Integer timeSlice) {
        this.memory = new MemoryWord[40];
        this.inputMutex = new Mutex();
        this.outputMutex = new Mutex();
        this.fileMutex = new Mutex();
        this.scheduler = new Scheduler(timeSlice);
        memory = new MemoryWord[40];
        for (int i = 0; i < 40; i++) {
            memory[i] = new MemoryWord();
        }
    }


    // Memory methods

    public void createProcess(String program) {
        // create the PCB

        // give the process a pid
        Integer pid = getNextPid();
        // give the process a state
        State state = State.READY;
        // give the process a program counter
        Integer programCounter = 0;
        // give the process a memory begin
        Integer memoryBegin = null;
        // give the process a memory end
        Integer memoryEnd = null;

        // insert PCB into memory
        Integer pcbPosition = pid * 5;
        memory[pcbPosition].setVariableName("pid");
        memory[pcbPosition].setValue(pid);

        memory[pcbPosition + 1].setVariableName("state");
        memory[pcbPosition + 1].setValue(state);

        memory[pcbPosition + 2].setVariableName("programCounter");
        memory[pcbPosition + 2].setValue(programCounter);

        memory[pcbPosition + 3].setVariableName("memoryBegin");
        memory[pcbPosition + 3].setValue(memoryBegin);

        memory[pcbPosition + 4].setVariableName("memoryEnd");
        memory[pcbPosition + 4].setValue(memoryEnd);

        // insert process into memory
        insertProcessIntoMemory(program, pid);
    }

    public void insertProcessIntoMemory(String program, Integer pid) {
        Integer position = memoryHasSpace();
        if (position != -1) {
            insertInstructionsIntoMemory(position, pid, program);
//            scheduler.addProcess(process);
        } else {
            int processToSwap = getProcessToSwap();
            int processToSwapMemoryBegin = getMemoryBegin(processToSwap);
            removeFromMemory(processToSwap);
            insertInstructionsIntoMemory(pid, processToSwapMemoryBegin, program);
        }
    }

    public Integer memoryHasSpace() {  // returns -1 if no empty spaces
        if (memory[firstProgram].getInstruction() == null)// && taken[26]==false) //first slot of process is empty + maybe 26 wont be used
            return firstProgram;
        else if (memory[secondProgram].getInstruction() == null)// && taken[39]==false) //second slot of process is empty + maybe 39 wont be used
            return secondProgram;
        else
            return -1; // both slots are occupied
    }

    public void insertInstructionsIntoMemory(Integer pid, Integer memoryPosition, String program) { // position read from pcv attribute?
        // TODO handle program attribute in process for this logic to work
        String[] programSplit = program.split("\n");

        Integer instructionStart = memoryPosition;

        for (int memoryLoc = instructionStart; memoryLoc < (programSplit.length + instructionStart); memoryLoc++)
            memory[memoryLoc].setInstruction(programSplit[memoryLoc - instructionStart]); // memoryLoc - instructionStart to start from 0

        Integer pcbStart = pid * 5;
        memory[pcbStart + 3].setValue(memoryPosition); // memory begin
        memory[pcbStart + 4].setValue(memoryPosition + 11); // memory end
    }

    public void insertVariablesIntoMemory(Integer pid, String variables) {
        String[] variablesSplit = variables.split("\n");
        Integer memoryEnd = getMemoryEnd(pid);
        Integer variablesStart = memoryEnd - 2;
        for (int memoryLoc = variablesStart; memoryLoc <= memoryEnd; memoryLoc++) {
            String[] lineSplit = variablesSplit[memoryLoc - variablesStart].split(" "); // memoryLoc - variablesStart to start from 0
            String variableName = lineSplit[1];
            String variableValue = lineSplit[2];
            memory[memoryLoc].setVariableName(variableName);
            memory[memoryLoc].setValue(variableValue);
        }
    }

    public void removeFromMemory(Integer pid) { //deletes process from memory by nullifying its segment
        Integer begin = getMemoryBegin(pid); // returns memory bound 1
        Integer end = getMemoryEnd(pid); // returns memory bound 2

        swapProcessToDisk(pid);
        for (int i = begin; i <= end; i++) //nullifies position in memory
            memory[i].clearWord();


        // nullify memory bounds in PCB
        Integer pcbStart = pid * 5;
        memory[pcbStart + 3].setValue(null); // nullify memory begin
        memory[pcbStart + 4].setValue(null); // nullify memory end
    }

    public void swapProcessToDisk(Integer pid) { // memory to disk
        Integer begin = getMemoryBegin(pid); // returns memory bound 1
        Integer end = getMemoryEnd(pid); // returns memory bound 2

        String content = pid + "\n";
        for (int i = begin; i <= end; i++) {
            String word = memory[i].toString();
            if (!word.equals("Empty"))
                content += word + "\n";
        }
        content += "__ENDPROCESS__\n";
        try {
            File file = new File("MemoryOnDisk.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void swapProcessToMemory(Integer pid) { // disk to memory
        String instructions = "";
        String variables = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("MemoryOnDisk.txt")));
            String line = br.readLine();
            while (line != null) {
                if (line.replaceAll("\n", "").equals(pid.toString())) {
                    line = br.readLine();
                    while (!line.replaceAll("\n", "").equals("__ENDPROCESS__")) {
                        String[] tokens = line.split(" ");
                        if (tokens[0].equals("variable")) {
                            variables += line + "\n";
                        } else {
                            instructions += line + "\n";
                            line = br.readLine();
                        }
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
        insertProcessIntoMemory(instructions, pid);
    }

    public void printMemory() {
        for (int i = 0; i < 40; i++) {
            System.out.println(memory[i].toString());
        }
    }

    public Integer getNextPid() {
        for (int i = 0; i < 3; i++) {
            if (getProcessState(i) == null) {
                return i;
            }
        }
        return -1;
    }

    public Integer getProcessToSwap() {
        for (int i = 0; i < 3; i++) {
            if (getProcessState(i) != State.RUNNING && getMemoryBegin(i) != null) {
                return i;
            }
        }
        return -1;
    }

    // PCB info getters
    public State getProcessState(Integer pid) {
        // get state from memory
        Integer pcbStart = pid * 5;
        return (State) memory[pcbStart + 1].getValue();
    }

    public void setProcessState(Integer pid, State state) {
        // set state in memory
        Integer pcbStart = pid * 5;
        memory[pcbStart + 1].setValue(state);
    }

    public Integer getProgramCounter(Integer pid) {
        // get program counter from memory
        Integer pcbStart = pid * 5;
        return (Integer) memory[pcbStart + 2].getValue();
    }

    public Integer getMemoryBegin(Integer pid) {
        // get memory begin from memory
        Integer pcbStart = pid * 5;
        return (Integer) memory[pcbStart + 3].getValue();
    }

    public Integer getMemoryEnd(Integer pid) {
        // get memory end from memory
        Integer pcbStart = pid * 5;
        return (Integer) memory[pcbStart + 4].getValue();
    }
}
