package Processes;

public class PriorityProcess extends Process {
    private int waitingTime;
    public PriorityProcess(String name, int arrivalTime, int burstTime,
                           int priority, String color) {
        super(name, arrivalTime, burstTime, priority, color);
    }
    public int getTotalWaitingTime() {return waitingTime;}
    public void setTotalWaitingTime(int waitingTime) {this.waitingTime = waitingTime;}
}
