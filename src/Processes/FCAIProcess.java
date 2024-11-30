package Schedulers;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int lastBurstTime;
    private int remainingBurstTime;
    private int priority;
    private int quantum;
    private int lastFinishTime;
    private String color;
    private List<Integer> quantumHistory;
    private boolean isPreempted;

    public Process(String name, int arrivalTime, int burstTime,
                   int priority, int quantum, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.color = color;
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

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
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

    public int getPriority() {
        return priority;
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

