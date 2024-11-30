package Schedulers;

import Processes.FCAIProcess;

import java.util.List;

public abstract class Scheduler {
    protected List<FCAIProcess> processList;

    public Scheduler(List<FCAIProcess> ProcessList) {
        this.processList = ProcessList;
    }

    public abstract void run();

    protected abstract void calculateAndPrint(List<FCAIProcess> ProcessList);
}
