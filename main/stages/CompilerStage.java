package main.stages;

public abstract class CompilerStage {
    protected String errorMessage = null;

    // Returns true if success, false if error
    public abstract boolean process(String line);

    public String getErrorMessage() {
        return errorMessage;
    }
}
