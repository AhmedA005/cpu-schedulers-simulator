package Processes;

public class ShortestRemainingTimeProcess extends Process{

    String state;
    int waitingTime=0;
    int TurnaroundTime=0;
    int executiontime=0;
    int agingFactor=1;
    int contextSwitching;

    public ShortestRemainingTimeProcess(String name, int arrivalTime, int burstTime, int priority, String color, String state, int contextSwitching) {
        super(name, arrivalTime, burstTime, priority, color);
        this.state=state;
        this.contextSwitching=contextSwitching;
    }

    public String getState(){ return state;}

    public void setState(String newState){ this.state= newState;}

    public void decrementBurstTime(){ this.burstTime--;}

    public void incrementWaitingTime(){ this.waitingTime++;}

    public int getWaitingTime(){ return waitingTime;}

    public void set_TurnaroundTime(int new_TurnaroundTime){ this.TurnaroundTime= new_TurnaroundTime;}

    public int get_TurnaroundTime(){ return TurnaroundTime;}

    public void incrementexecutionTime(){ this.executiontime++;}

    public int getexecutionTime(){ return executiontime;}

    public int get_EffectiveBurstTime(int nowTime){
        int waitingTime2=nowTime-this.getArrivalTime();
        if (waitingTime2 > 2 * this.getBurstTime()) {
            return Math.max(1, this.getBurstTime() - agingFactor * waitingTime2);
        }
        return this.getBurstTime();
    }
    public int getcontextSwitching(){ return contextSwitching;}

}
