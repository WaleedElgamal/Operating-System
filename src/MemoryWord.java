package src;

public class MemoryWord {
    private String variableName;
    private Object value;
    private String instruction;



    public MemoryWord(String variableName, Object value) {
        this.variableName = variableName;
        this.value = value;
    }
    public MemoryWord(String instruction){
        this.instruction = instruction;
    }
    public MemoryWord(){
        variableName = null;
        value = null;
        instruction = null;
    }

    public void clearWord(){
        variableName = null;
        value = null;
        instruction = null;
    }

    public String toString(){
        if (variableName != null && value != null){
            return "variable " + variableName + " = " + value;
        }
        else if (instruction != null){
            return instruction;
        }
        else{
            return "Empty";
        }
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getInstruction() {
        return instruction;
    }
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
