package Processes;

public class PriorityProcess extends Process {
    private int waitingTime;
    private int turnaroundTime;
    public PriorityProcess(String name, int arrivalTime, int burstTime,
                           int priority, String color) {
        super(name, arrivalTime, burstTime, priority, color);
    }
    public int getTotalWaitingTime() {return waitingTime;}
    public void setTotalWaitingTime(int waitingTime) {this.waitingTime = waitingTime;}
    public int getTurnaroundTime() {return turnaroundTime;}
    public void setTurnaroundTime(int turnaroundTime) {this.turnaroundTime = turnaroundTime;}
}
