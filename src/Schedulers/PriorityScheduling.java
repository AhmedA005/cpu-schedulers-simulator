package Schedulers;

import Processes.PriorityProcess;
import Processes.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriorityScheduling extends Scheduler {
    public List<Processes.Process> finishedProcesses;
    public int size;
    int time;
    int counter;
    public float avgWaitingTime;
    public float avgTurnaroundTime;
    float contextSwitchTime;


    public PriorityScheduling(List<Processes.Process> processList, float contextSwitchTime) {
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
            for (Processes.Process process : processList) {
                Processes.PriorityProcess p = (Processes.PriorityProcess) process; // Correct cast
                if (p.getArrivalTime() <= time) {
                    flag = true;
                    time += contextSwitchTime;
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
    protected void calculateAndPrint(List<Processes.Process> ProcessList) {
        for (Processes.Process process : finishedProcesses) { // Declare the process variable
            System.out.println("Process: " + process.getName());
            System.out.println("Waiting time: " + ((PriorityProcess) process).getTotalWaitingTime());
            System.out.println("Turnaround time: " + ((PriorityProcess) process).getTurnaroundTime());
            System.out.println();
        }
        System.out.println("Average waiting time: " + avgWaitingTime / size);
        System.out.println("Average turnaround time: " + avgTurnaroundTime / size);
    }
}


