package Schedulers;

import Processes.Process;

import java.util.List;

public class ShortestJobFirst extends Scheduler {
    public ShortestJobFirst(List<Process> ProcessList) {
        super(ProcessList);
    }

    @Override
    public void run() {

    }


    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {

    }
}
