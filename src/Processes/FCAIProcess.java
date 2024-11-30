package Processes;

import java.util.ArrayList;
import java.util.List;

public class FCAIProcess extends Process {

    private int lastBurstTime;
    private int remainingBurstTime;
    private int quantum;
    private int lastFinishTime;
    private List<Integer> quantumHistory;
    private boolean isPreempted;

    public FCAIProcess(String name, int arrivalTime, int burstTime,
                       int priority, int quantum, String color) {
        super(name, arrivalTime, burstTime, priority, color);
        this.remainingBurstTime = burstTime;
        this.quantum = quantum;
        this.lastFinishTime = arrivalTime;
        this.quantumHistory = new ArrayList<>();
        this.quantumHistory.add(quantum);
        this.isPreempted = false;
    }

    public void execute(int executionTime) {
        lastFinishTime = burstTime;
        remainingBurstTime -= executionTime;
        lastFinishTime += executionTime;
    }


    public int getBurstTime() {
        return burstTime;
    }

    public int getLastBurstTime() {
        return lastBurstTime;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }


    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
        this.quantumHistory.add(quantum);
    }

    public int getLastFinishTime() {
        return lastFinishTime;
    }

    public List<Integer> getQuantumHistory() {
        return quantumHistory;
    }

    public void setPreempted(boolean isPreempted) {
        this.isPreempted = isPreempted;
    }

    public boolean isPreempted() {
        return isPreempted;
    }
}

