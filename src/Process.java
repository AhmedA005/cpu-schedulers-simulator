import java.util.ArrayList;
import java.util.List;

public class Process implements Runnable {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int remainingBurstTime;
    private int priority;
    private int quantum;
    private int lastFinishTime;
    private int totalWaitingTime;
    private String color; // Optional
    private List<Integer> quantumHistory; // Tracks quantum history

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.color = color;
        this.lastFinishTime = arrivalTime;
        this.totalWaitingTime = 0;
        this.quantumHistory = new ArrayList<>();
        this.quantumHistory.add(quantum);
    }

    public void setLastFinishTime(int lastFinishTime) {
        this.lastFinishTime = lastFinishTime;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
        this.quantumHistory.add(quantum);
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

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getQuantum() {
        return quantum;
    }

    public int getLastFinishTime() {
        return lastFinishTime;
    }

    public int getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public List<Integer> getQuantumHistory() {
        return quantumHistory;
    }

    public String getColor() {
        return color;
    }

    @Override
    public void run() {

    }
}

