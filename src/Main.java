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
        List<Process> PriorityProcesses = new ArrayList<>();
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

        List<Process> FCAIProcesses = new ArrayList<>();
        FCAIProcesses.add(new FCAIProcess("P1", 0, 17, 4, 4, "red"));
        FCAIProcesses.add(new FCAIProcess("P2", 3, 6, 9, 3, "yellow"));
        FCAIProcesses.add(new FCAIProcess("P3", 4, 10, 3, 5, "blue"));
        FCAIProcesses.add(new FCAIProcess("P4", 29, 4, 10, 2, "black"));
        FCAIScheduling fcaiScheduling = new FCAIScheduling(FCAIProcesses);

        fcaiScheduling.run();

        List<Process> shortestRemainingTimeProcesses = new ArrayList<>();
        shortestRemainingTimeProcesses.add( new ShortestRemainingTimeProcess("P1", 0, 4, 4, "red", 1));
        shortestRemainingTimeProcesses.add( new ShortestRemainingTimeProcess("P2", 1, 8, 3, "green", 1));
        shortestRemainingTimeProcesses.add( new ShortestRemainingTimeProcess("P3", 3, 2, 6, "blue", 1));
        shortestRemainingTimeProcesses.add( new ShortestRemainingTimeProcess("P4", 10, 6, 9, "gray", 1));
        shortestRemainingTimeProcesses.add( new ShortestRemainingTimeProcess("P5", 12, 5, 10, "black", 1));
        ShortestRemainingTimeFirst srtf=new ShortestRemainingTimeFirst(shortestRemainingTimeProcesses);
        srtf.run();
    }

}
