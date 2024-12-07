package Processes;

import java.util.ArrayList;
import java.util.List;

public class FCAIProcess extends Process {

    private int remainingBurstTime;
    private int quantum;
    private int finalFinishTime;
    private List<Integer> quantumHistory;
    private boolean isPreempted;

    public FCAIProcess(String name, int arrivalTime, int burstTime,
                       int priority, int quantum, String color) {
        super(name, arrivalTime, burstTime, priority, color);
        this.remainingBurstTime = burstTime;
        this.quantum = quantum;
        this.quantumHistory = new ArrayList<>();
        this.quantumHistory.add(quantum);
        this.isPreempted = false;
    }

    public void execute(int executionTime) {
        remainingBurstTime -= executionTime;
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

    public List<Integer> getQuantumHistory() {
        return quantumHistory;
    }

    public void setPreempted(boolean isPreempted) {
        this.isPreempted = isPreempted;
    }

    public boolean isPreempted() {
        return isPreempted;
    }

    public int getFinalFinishTime() {
        return finalFinishTime;
    }

    public void setFinalFinishTime(int finalFinishTime) {
        this.finalFinishTime = finalFinishTime;
    }

}

