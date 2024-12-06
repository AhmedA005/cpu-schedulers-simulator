package Processes;

public class ShortestJobFirstProcess extends Process{
    private int waitingTime;
    private int turnAroundTime;
    public ShortestJobFirstProcess(String name, int arrivalTime, int burstTime, int priority, String color) {
        super(name, arrivalTime, burstTime, priority, color);
    }
    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
}
