package Schedulers;

import java.util.List;

public class ShortestJobFirst extends Scheduler{
    public ShortestJobFirst(List<Process> processList) {
        super(processList);
    }

    @Override
    public void run() {

    }
}