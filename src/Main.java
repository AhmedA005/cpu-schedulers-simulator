import Processes.PriorityProcess;
import Processes.Process;
import Processes.ShortestJobFirstProcess;
import Schedulers.ShortestJobFirst;
import Schedulers.FCAIScheduling;

import java.util.ArrayList;
import java.util.List;

import Processes.FCAIProcess;
import Schedulers.PriorityScheduling;

public class Main {
    public static void main(String[] args) {
        List<Processes.Process> PriorityProcesses = new ArrayList<>();
        PriorityProcesses.add(new ShortestJobFirstProcess("P1", 0, 4, 0, "red"));
        PriorityProcesses.add(new ShortestJobFirstProcess("P2", 1, 8, 1, "blue"));
        PriorityProcesses.add(new ShortestJobFirstProcess("P3", 3, 2, 2, "yellow"));
        /*PriorityProcesses.add(new PriorityProcess("P4", 10, 6, 3, "white"));
        PriorityProcesses.add(new PriorityProcess("P5", 12, 5, 4, "white"));
        PriorityProcesses.add(new PriorityProcess("P1", 0, 4, 0, "red"));
        PriorityProcesses.add(new PriorityProcess("P2", 1, 8, 1, "blue"));
        PriorityProcesses.add(new PriorityProcess("P3", 3, 2, 2, "yellow"));
        PriorityProcesses.add(new PriorityProcess("P4", 10, 6, 3, "white"));
        PriorityProcesses.add(new PriorityProcess("P5", 12, 5, 4, "white"));*/

        float contextSwitchTime = 5;

        ShortestJobFirst priorityScheduler = new ShortestJobFirst(PriorityProcesses);
        priorityScheduler.run();
    }
}