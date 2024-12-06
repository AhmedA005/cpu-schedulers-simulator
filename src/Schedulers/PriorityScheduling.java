package Schedulers;

import Processes.PriorityProcess;
import Processes.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriorityScheduling extends Scheduler {
    List<Process> finishedProcesses;
    int size;
    int time;
    int counter;
    float avgWaitingTime;
    float avgTurnaroundTime;
    float contextSwitchTime;


    public PriorityScheduling(List<Process> processList, int contextSwitchTime) {
        super(processList); // Initialize parent class's processList
        this.processList = processList; // Explicitly initialize shadowed field
        this.size = processList.size(); // Dynamically calculate size
        this.time = 0;
        this.counter = 0;
        this.avgWaitingTime = 0;
        finishedProcesses = new ArrayList<>();
        this.avgTurnaroundTime = 0;
        this.contextSwitchTime = contextSwitchTime;
    }


    public void run() {
        processList.sort(Comparator.comparingInt(Process::getPriority));
        while (counter < size) {
            boolean flag = false;
            for (Process process : processList) {
                PriorityProcess p = (PriorityProcess) process;
                if (p.getArrivalTime() <= time) {
                    flag = true;
                    time+= contextSwitchTime;
                    p.setTotalWaitingTime(time - p.getArrivalTime());
                    avgWaitingTime += p.getTotalWaitingTime();
                    time += p.getBurstTime();
                    p.setTurnaroundTime(time - p.getArrivalTime());
                    avgTurnaroundTime += p.getTurnaroundTime();
                    finishedProcesses.add(p);
                    processList.remove(p);
                    counter++;
                    break;
                }
            }
            if (!flag)
                time++;
        }
        calculateAndPrint(finishedProcesses);
    }

    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {
        for (Process process : finishedProcesses) {
            System.out.println("Process: " + process.getName());
            System.out.println("Waiting time: " + ((PriorityProcess)process).getTotalWaitingTime());
            System.out.println("Turnaround time: " + ((PriorityProcess)process).getTurnaroundTime());
            System.out.println();
        }
        System.out.println("Average waiting time: " + avgWaitingTime / size);
        System.out.println("Average turnaround time: " + avgTurnaroundTime / size);
    }
}
