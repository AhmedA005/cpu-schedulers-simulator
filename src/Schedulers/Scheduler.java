package Schedulers;

import Processes.Process;

import java.util.List;

public abstract class Scheduler {
    protected List<Process> processList;

    public Scheduler(List<Process> ProcessList) {
        this.processList = ProcessList;
    }

    public abstract void run();

    protected abstract void calculateAndPrint(List<Process> ProcessList);
}
