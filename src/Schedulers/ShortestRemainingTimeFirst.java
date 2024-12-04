package Schedulers;

import Processes.Process;
import Processes.ShortestRemainingTimeProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.ceil;

public class ShortestRemainingTimeFirst extends Scheduler {
    List<ShortestRemainingTimeProcess> arrivedList;
    List<ShortestRemainingTimeProcess> tempList;

    public ShortestRemainingTimeFirst(List<Process> ProcessList) {
        super(ProcessList);
        this.arrivedList= new ArrayList<>();
        this.tempList= new ArrayList<>();

    }
    @Override
    public void run() {

        int nowTime=0;
        ShortestRemainingTimeProcess startProcess = null;
        ShortestRemainingTimeProcess firstArrive=null;
        ShortestRemainingTimeProcess currentProcess = null;
        int agingFactor=10;

        for(int i=0; i< processList.size();i++){
            tempList.add((ShortestRemainingTimeProcess) processList.get(i));
            ((ShortestRemainingTimeProcess) processList.get(i)).set_originalBurstTime(((ShortestRemainingTimeProcess) processList.get(i)).getBurstTime());
        }

        while(!processList.isEmpty()){

            boolean switched=false;
            for(int i=0; i< processList.size();i++){
                firstArrive= (ShortestRemainingTimeProcess) processList.get(i);
                if(firstArrive.getArrivalTime()<=nowTime && !arrivedList.contains(firstArrive)){
                    arrivedList.add(firstArrive);
                }
            }

            startProcess=toStartMinProcess(arrivedList, nowTime);

            if(startProcess!= null){

                if(currentProcess != startProcess){
                    if (currentProcess != null) {
                        nowTime += startProcess.getcontextSwitching();
                        switched=true;
                    }
                    currentProcess = startProcess;
                    for( ShortestRemainingTimeProcess process: arrivedList) {
                        if(!process.equals(startProcess)) {
                            process.setState("waiting");
                            process.incrementWaitingTime();
                        }
                    }

                }
                startProcessFunction(startProcess);
                nowTime++;
                startProcess.decrementBurstTime();

                if(startProcess.getBurstTime()==0){
                    startProcess.setState("Finished");
                    startProcess.set_TurnaroundTime(nowTime-startProcess.getArrivalTime());//completion-arrival
                    startProcess.set_WaitingTime(startProcess.get_TurnaroundTime()-startProcess.get_originalBurstTime());
                    processList.remove(startProcess);
                    arrivedList.remove(startProcess);
                }

            }else{
                System.out.println("No process to start!");
                nowTime++;
            }

        }
        calculateAndPrint(processList);

    }

    public ShortestRemainingTimeProcess toStartMinProcess(List<ShortestRemainingTimeProcess> FindMin , int now_time){
        int min_time=Integer.MAX_VALUE;
        int Minburst_time=0;
        ShortestRemainingTimeProcess toStartProcess = null;
        ShortestRemainingTimeProcess FinaltoStartProcess = null;

        for (ShortestRemainingTimeProcess shortestRemainingTimeProcess : FindMin) {
            toStartProcess = shortestRemainingTimeProcess;
            Minburst_time = toStartProcess.get_EffectiveBurstTime(now_time);
            if (Minburst_time<min_time) {
                min_time = Minburst_time;
                FinaltoStartProcess = toStartProcess;
            }
        }
        return FinaltoStartProcess;
    }


    public void startProcessFunction(ShortestRemainingTimeProcess process_toStart){
        process_toStart.setState("start");
    }

    @Override
    protected void calculateAndPrint(List<Process> processList) {

        double totalWaitingTime = 0;
        int averageWaitingTime = 0;
        double totalTurnaroundTime=0;
        int averageTurnaroundTime=0;

        for (ShortestRemainingTimeProcess process : tempList) {
            System.out.println("process : " + process.getName() + " waiting time = " + process.getWaitingTime());
        }

        for (ShortestRemainingTimeProcess process : tempList) {
            System.out.println("process : " + process.getName() + " Turnaround time = " + process.get_TurnaroundTime());
        }

        for (ShortestRemainingTimeProcess process : tempList) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime+=process.get_TurnaroundTime();
        }

        averageWaitingTime= (int) ceil(totalWaitingTime/tempList.size());
        averageTurnaroundTime=(int) ceil(totalTurnaroundTime/tempList.size());
        System.out.println("Average waiting time = " + averageWaitingTime);
        System.out.println("Average Turnaround time = " + averageTurnaroundTime);

    }
}
