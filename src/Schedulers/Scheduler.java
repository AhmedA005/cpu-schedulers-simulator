package Schedulers;

import java.util.List;

public abstract class Scheduler {
    protected List<Process> processList;

    public Scheduler(List<Process> processList) {
        this.processList = processList;
    }
    public abstract void run();
    protected void calculateAndPrint(List<Process> processList) {}
}
