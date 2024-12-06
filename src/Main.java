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
        FCAIProcesses.add(new PriorityProcess("P1", 0, 4, 0, "red"));
        FCAIProcesses.add(new PriorityProcess("P2", 1, 8, 1, "blue"));
        FCAIProcesses.add(new PriorityProcess("P3", 3, 2, 2, "yellow"));
        FCAIProcesses.add(new PriorityProcess("P4", 10, 6, 3, "white"));
        FCAIProcesses.add(new PriorityProcess("P5", 12, 5, 4, "white"));

        int contextSwitchTime = 5;

        FCAIScheduling scheduler = new FCAIScheduling(FCAIProcesses);
        PriorityScheduling priorityScheduler = new PriorityScheduling(FCAIProcesses, contextSwitchTime);
        priorityScheduler.run();
//        scheduler.run();
    }
}
