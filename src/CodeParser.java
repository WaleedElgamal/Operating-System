package src;

public class CodeParser {

    private Scheduler scheduler;

    public CodeParser(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void parseLine(String line, int pid, Object[] tempValue) {
        String[] tokens = line.split(" ");
        String[] variables = getProcessVariables(pid);
        switch (tokens[0]) {
            case "semWait": {
                switch (tokens[1]) {
                    case "userInput": {
                        semWait(OperatingSystem.getInputMutex(), pid);
                        break;
                    }
                    case "userOutput": {
                        semWait(OperatingSystem.getOutputMutex(), pid);
                        break;
                    }
                    case "file": {
                        semWait(OperatingSystem.getFileMutex(), pid);
                        break;
                    }
                }
                break;
            }
            case "semSignal": {
                switch (tokens[1]) {
                    case "userInput": {
                        semSignal(OperatingSystem.getInputMutex(), pid);
                        break;
                    }
                    case "userOutput": {
                        semSignal(OperatingSystem.getOutputMutex(), pid);
                        break;
                    }
                    case "file": {
                        semSignal(OperatingSystem.getFileMutex(), pid);
                        break;
                    }
                }
                break;
            }
            case "print": {
                String value = tokens[1];
                for (int i = 0; i < variables.length; i++) {
                    if (variables[i] == null) // TODO: add this to other loops
                        break;
                    if (variables[i].equals(value)) {
                        value = (String) getVariableValue(pid, value);
                        break;
                    }
                }
                print(value);
                // TODO: handle if "value" is a system call (e.g: print input)
                break;
            }
            case "printFromTo": {
                Object from = tokens[1];
                Object to = tokens[2];
                for (int i = 0; i < variables.length; i++) {
                    if (variables[i] == null) // TODO: add this to other loops
                        break;
                    if (variables[i].equals((String) (from + ""))) {
                        from = getVariableValue(pid, (String) from);
                    } else if (variables[i].equals((String) (to + ""))) {
                        to = getVariableValue(pid, (String) to);
                    }
                }
                try {
                    printFromTo((Integer) from, (Integer) to);
                } catch (NumberFormatException e) {
                    print("Invalid input");
                }
                break;
            }
            case "writeFile": {
                String filename = tokens[1];
                String content = tokens[2];
                for (int i = 0; i < variables.length; i++) {
                    if (variables[i] == null) // TODO: add this to other loops
                        break;
                    if (variables[i].equals(content)) {
                        content = (String) (getVariableValue(pid, (String) content) + "");
                    }else if (variables[i].equals(filename)) {
                        filename = (String) (getVariableValue(pid, (String) filename) + "");
                    }
                }
                writeFile(filename, content);
                break;
            }
            case "readFile": {
                String filename = tokens[1];
                for (int i = 0; i < variables.length; i++) {
                    if (variables[i] == null) // TODO: add this to other loops
                        break;
                    else if (variables[i].equals(filename)) {
                        filename = (String) (getVariableValue(pid, (String) filename) + "");
                        break;
                    }
                }
                tempValue[pid] = readFile(filename);
                break;
            }
            case "assign": {
                String variable = tokens[1];
                Object value = OperatingSystem.parseString(tokens[2]);
                if (tempValue[pid] != null) { // if we have a value from previous time slice
                    value = tempValue[pid];
                    tempValue[pid] = null; // set it to null so we don't use it again
                } else {
                    switch ((String) value) {
                        case "input": {
                            // first time slice so we do input first
                            tempValue[pid] = getInput("Enter value for " + variable);
                            break;
                        }
                        case "readFile": {
                            //first time slice so we do readfile first
                            String filename = tokens[3];
                            for (int i = 0; i < variables.length; i++) {
                                if (variables[i] == null) // TODO: add this to other loops
                                    break;
                                else if (variables[i].equals(filename)) {
                                    filename = (String) (getVariableValue(pid, (String) filename) + "");
                                    break;
                                }
                            }
                            tempValue[pid] = OperatingSystem.parseString(readFile(filename));
                            break;
                        }
                        default: {
                            for (int i = 0; i < variables.length; i++) {
                                if (variables[i] == null) // TODO: add this to other loops
                                    break;
                                if (variables[i].equals(value)) {
                                    value = getVariableValue(pid, (String) value);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (tempValue[pid] == null)
                    assign(variable, value, pid);
                break;
            }
        }
    }

    private void printFromTo(int from, int to) {
        String res = "";
        for (int i = from + 1; i < to; i++) { // +1 and < to make it exclusive
            res += i + " ";
        }
        SystemCalls.print(res);
    }

    private void print(String message) {
        SystemCalls.print(message);
    }

    private void semWait(Mutex resource, int pid) {
        resource.semWait(pid, scheduler);
    }

    private void semSignal(Mutex resource, int pid) {
        resource.semSignal(pid, scheduler);
    }

    private void assign(String variableName, Object value, int pid) {
        setProcessVariable(variableName, value, pid);
    }

    private Object getInput(String prompt) {
        return OperatingSystem.parseString(SystemCalls.getInput(prompt));
    }

    private void writeFile(String filename, String content) {
        SystemCalls.writeFile(filename, content);
    }

    private String readFile(String filename) {
        return SystemCalls.readFile(filename);
    }

    public String[] getProcessVariables(int pid) {
        Integer pcbStart = pid * 5;
        Integer end = (Integer) SystemCalls.readMem(pcbStart + 4).getValue();
        Integer variableBegin = end - 2;
        String[] variables = new String[3];
        for (int i = variableBegin; i < end; i++) {
            if (SystemCalls.readMem(i) == null)     //variables[i - variableBegin] == null)
                break;
            variables[i - variableBegin] = SystemCalls.readMem(i).getVariableName();
        }
        return variables;
    }

    public Object getVariableValue(int pid, String variableName) {
        Integer pcbStart = pid * 5;
        Integer end = (Integer) SystemCalls.readMem(pcbStart + 4).getValue(); // returns memory bound 2
        for (int i = end; i >= end - 2; i--) {
            MemoryWord mem = SystemCalls.readMem(i);
            if (variableName.equals(mem.getVariableName()))
                return mem.getValue();
        }
        return null;
    }

    public void setProcessVariable(String variableName, Object value, int pid) {
        Integer pcbStart = pid * 5;
        Integer end = (Integer) SystemCalls.readMem(pcbStart + 4).getValue();
        Integer variableBegin = end - 2;
        for (int i = variableBegin; i <= end; i++) {
            MemoryWord mem = SystemCalls.readMem(i);
            if (variableName.equals(mem.getVariableName())) { // if variable already exists
                mem.setValue(value);
                break;
            }
            if (mem.getVariableName() == null) { // creating new variable
                SystemCalls.writeMem(new MemoryWord(variableName, value), i);
                break;
            }
        }
    }
}
