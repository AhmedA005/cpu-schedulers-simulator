package Schedulers;

import Processes.PriorityProcess;
import Processes.Process;

import java.util.Comparator;
import java.util.List;

public class PriorityScheduling extends Scheduler {
    List<PriorityProcess> finishedProcesses;
    int size;
    int time;
    int counter;
    float avgwaitingTime;


    public PriorityScheduling(List<Process> processList) {
        super(processList); // Initialize parent class's processList
        this.processList = processList; // Explicitly initialize shadowed field
        this.size = processList.size(); // Dynamically calculate size
        this.time = 0;
        this.counter = 0;
        this.avgwaitingTime = 0;
    }


    public void run() {
        processList.sort(Comparator.comparingInt(Process::getPriority));
        while (counter < size) {
            boolean flag = false;
            for (Process process : processList) {
                PriorityProcess p = (PriorityProcess) process;
                if (p.getArrivalTime() <= time) {
                    flag = true;
                    p.setTotalWaitingTime(time - p.getArrivalTime());
                    avgwaitingTime += p.getTotalWaitingTime();
                    time += p.getBurstTime();
                    finishedProcesses.add(p);
                    processList.remove(p);
                    counter++;
                    break;
                }
            }
            if (!flag)
                time++;
        }
    }

    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {
        for (PriorityProcess process : finishedProcesses) {
            System.out.println("Process: " + process.getName());
            System.out.println("Waiting time: " + process.getTotalWaitingTime());
        }
        System.out.println("Average waiting time: " + avgwaitingTime / size);
    }
}