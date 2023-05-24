package src;

public class CodeParser {

    private Scheduler scheduler;

    public CodeParser(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void parseLine(String line, Process process) {
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
            case "semWait":
                break;
            case "semSignal":
                break;
            case "assign":
                break;
            case "print": {
                String value = tokens[1];
                // TODO: handle if "value" is a system call
                break;
            }
            case "printFromTo":
                break;
            case "writeFile":
                break;
            default:
                break;
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

    private void semWait(Mutex resouce, int pid) {
        resouce.semWait(pid, scheduler);
    }

    private void semSignal(Mutex resouce, int pid) {
        resouce.semSignal(pid, scheduler);
    }

    private void assign(String variableName, int value) {
        // scheduler.getProcess().getMemory().get(variableName).setValue(value);
        // TODO: implement
    }

    private void writeFile(String filename, String content) {
        SystemCalls.writeFile(filename, content);
    }

    private String readFile(String filename) {
        return SystemCalls.readFile(filename);
    }
}
