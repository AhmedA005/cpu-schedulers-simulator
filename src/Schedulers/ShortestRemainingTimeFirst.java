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

        for(int i=0; i< processList.size();i++){
            tempList.add((ShortestRemainingTimeProcess) processList.get(i));
        }

        while(!processList.isEmpty()){

            for(int i=0; i< processList.size();i++){
                firstArrive= (ShortestRemainingTimeProcess) processList.get(i);
                if(firstArrive.getArrivalTime()==nowTime && !arrivedList.contains(firstArrive)){
                    arrivedList.add(firstArrive);
                }
            }


            startProcess=toStartMinProcess(arrivedList, nowTime);

            for( ShortestRemainingTimeProcess process: arrivedList){
                if(!process.equals(startProcess)){
                    process.setState("waiting");
                    process.incrementWaitingTime();

                }
            }


            if(startProcess!= null){

                if (currentProcess != startProcess) {
                    nowTime += startProcess.getcontextSwitching();
                    currentProcess = startProcess;
                }
                else{
                    nowTime++;
                }

                startProcessFunction(startProcess);
//                nowTime++;
                startProcess.decrementBurstTime();
                if(startProcess.getState()=="start"){
                    startProcess.incrementexecutionTime();
                }
                if(startProcess.getBurstTime()==0){
                    startProcess.setState("Finished");
                    processList.remove(startProcess);
                    arrivedList.remove(startProcess);
                }

            }else{
                System.out.println("No process to start!");
            }

        }
        int turnaroundTime=0;
        for(int i=0; i< tempList.size();i++){
            turnaroundTime+=tempList.get(i).getWaitingTime()+tempList.get(i).getBurstTime();
            tempList.get(i).set_TurnaroundTime(turnaroundTime);
        }
        calculateAndPrint(processList);

    }


    public ShortestRemainingTimeProcess toStartMinProcess(List<ShortestRemainingTimeProcess> FindMin , int systemTime){
        int max_time=Integer.MAX_VALUE;
        int MinEffectiveburst_time=0;
        ShortestRemainingTimeProcess toStartProcess = null;
        ShortestRemainingTimeProcess FinaltoStartProcess = null;

        for (ShortestRemainingTimeProcess shortestRemainingTimeProcess : FindMin) {
            toStartProcess = shortestRemainingTimeProcess;
            MinEffectiveburst_time = toStartProcess.get_EffectiveBurstTime(systemTime);
            if (MinEffectiveburst_time < max_time) {
                max_time = MinEffectiveburst_time;
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
        int turnaroundtime=0;

        for (ShortestRemainingTimeProcess process : tempList) {
            System.out.println("process : " + process.getName() + " waiting time = " + process.getWaitingTime());
        }

        for (ShortestRemainingTimeProcess process : tempList) {
            turnaroundtime=process.getWaitingTime() + process.getexecutionTime();
            process.set_TurnaroundTime(turnaroundtime);
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
