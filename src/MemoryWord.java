package src;

public class MemoryWord {
    private String variableName;

    public MemoryWord(String variableName, Object value) {
        this.variableName = variableName;
        this.value = value;
    }

    private Object value;

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
}
