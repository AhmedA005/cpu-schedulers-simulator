package Processes;

public class ShortestRemainingTimeProcess extends Process{

    int waitingTime=0;
    int TurnaroundTime=0;
    int agingFactor=20;
    int contextSwitching;
    int originalBurstTime;

    public ShortestRemainingTimeProcess(String name, int arrivalTime, int burstTime, int priority, String color) {
        super(name, arrivalTime, burstTime, priority, color);
    }
    public int get_originalBurstTime(){
        return originalBurstTime;
    }
    public void set_originalBurstTime(int new_originalBurstTime){
        this.originalBurstTime=new_originalBurstTime;
    }


    public void decrementBurstTime(){ this.burstTime--;}

    public void incrementWaitingTime(){ this.waitingTime++;}

    public int getWaitingTime(){ return waitingTime;}
    public void set_WaitingTime(int new_WaitingTime){ this.waitingTime= new_WaitingTime;}

    public void set_TurnaroundTime(int new_TurnaroundTime){ this.TurnaroundTime= new_TurnaroundTime;}

    public int get_TurnaroundTime(){ return TurnaroundTime;}

    public ShortestRemainingTimeProcess starvedProcess(int nowTime){
        int waitingTime2=nowTime-this.getArrivalTime();
        if (waitingTime2 >= agingFactor) {
            return this;
        }
        return null;
    }
    public int getcontextSwitching(){ return contextSwitching;}
    public int getAgingFactor(){ return agingFactor;}
}

