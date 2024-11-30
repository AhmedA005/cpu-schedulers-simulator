package Schedulers;

import Processes.FCAIProcess;

import java.util.List;

public class PriorityScheduling extends Scheduler{
    public PriorityScheduling(List<FCAIProcess> ProcessList) {
        super(ProcessList);
    }

    @Override
    public void run() {

    }

    @Override
    protected void calculateAndPrint(List<FCAIProcess> ProcessList) {

    }
}