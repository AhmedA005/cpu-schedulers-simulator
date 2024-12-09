package Processes;

public class Process {
    private String name;
    private int arrivalTime;
    int burstTime;
    private int priority;
    private String color;
    int quantum;

    public Process(String name, int arrivalTime, int burstTime, int priority, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
    }
    public Process(String name, int arrivalTime, int burstTime, int priority,int quantum, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
        this.quantum = quantum;
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

    public int getPriority() {
        return priority;
    }

    public int getQuantum() { return quantum;}

    public String getColor() {
        return color;
    }
}
