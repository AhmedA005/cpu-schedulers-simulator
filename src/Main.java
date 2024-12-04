import Processes.PriorityProcess;
import Processes.Process;
import Schedulers.FCAIScheduling;

import java.util.ArrayList;
import java.util.List;

import Processes.FCAIProcess;
import Schedulers.PriorityScheduling;

public class Main {
    public static void main(String[] args) {
        List<Process> FCAIProcesses = new ArrayList<>();
        FCAIProcesses.add(new PriorityProcess("P1", 0, 17, 4, "red"));
        FCAIProcesses.add(new PriorityProcess("P2", 3, 6, 9, "blue"));
        FCAIProcesses.add(new PriorityProcess("P3", 4, 10, 3, "yellow"));
        FCAIProcesses.add(new PriorityProcess("P4", 29, 4, 10, "white"));

        FCAIScheduling scheduler = new FCAIScheduling(FCAIProcesses);
        PriorityScheduling priorityScheduler = new PriorityScheduling(FCAIProcesses);
        priorityScheduler.run();
//        scheduler.run();
    }
}