package Schedulers;

import Processes.Process;
import Processes.ShortestRemainingTimeProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.ceil;

public class ShortestRemainingTimeFirst extends Scheduler {
    List<ShortestRemainingTimeProcess> arrivedList;
    public List<ShortestRemainingTimeProcess> tempList;
    public List<ShortestRemainingTimeProcess> executionList;

    public ShortestRemainingTimeFirst(List<Process> ProcessList) {
        super(ProcessList);
        this.arrivedList= new ArrayList<>();
        this.tempList= new ArrayList<>();
        this.executionList= new ArrayList<>();

    }
    @Override
    public void run() {

        int nowTime=0;
        ShortestRemainingTimeProcess startProcess = null;
        ShortestRemainingTimeProcess firstArrive=null;
        ShortestRemainingTimeProcess currentProcess = null;
        int agingFactor=20;
        boolean switched=false;
        for(int i=0; i< processList.size();i++){
            tempList.add((ShortestRemainingTimeProcess) processList.get(i));
            ((ShortestRemainingTimeProcess) processList.get(i)).set_originalBurstTime(((ShortestRemainingTimeProcess) processList.get(i)).getBurstTime());
        }

        while(!processList.isEmpty()){
            switched=false;
            for(int i=0; i< processList.size();i++){
                firstArrive= (ShortestRemainingTimeProcess) processList.get(i);
                if(firstArrive.getArrivalTime()<=nowTime && !arrivedList.contains(firstArrive)){
                    arrivedList.add(firstArrive);
                }
            }

            startProcess=toStartMinProcess(arrivedList,nowTime);
            executionList.add(startProcess);
            
            if(startProcess!= null){
                if(currentProcess != startProcess){
                    if (currentProcess != null) {
                        nowTime += startProcess.getcontextSwitching();
                        switched=true;
                    }
                    currentProcess = startProcess;

                }
                nowTime++;
                startProcess.decrementBurstTime();

                if(startProcess.getBurstTime()==0){
                    startProcess.set_TurnaroundTime(nowTime+ startProcess.getcontextSwitching()-startProcess.getArrivalTime());//completion-arrival
                    startProcess.set_WaitingTime(startProcess.get_TurnaroundTime()-startProcess.get_originalBurstTime());
                    processList.remove(startProcess);
                    arrivedList.remove(startProcess);
                }

            }else{
                System.out.println("No process to start!");
                nowTime++;
            }

        }
        for (int i=0; i< executionList.size();i++) {
            if(i==executionList.size()-1){
                System.out.print(executionList.get(i).getName());
            }else{
                System.out.print(executionList.get(i).getName() + "-> " );
            }

        }
        System.out.println (" ");
        calculateAndPrint(processList);

    }
    public ShortestRemainingTimeProcess toStartMinProcess(List<ShortestRemainingTimeProcess> FindMin , int now_time){
        int min_time=Integer.MAX_VALUE;
        int Minburst_time=0;
        ShortestRemainingTimeProcess FinaltoStartProcess = null;

        for (ShortestRemainingTimeProcess shortestRemainingTimeProcess : FindMin) {
            ShortestRemainingTimeProcess ifStarved = shortestRemainingTimeProcess.starvedProcess(now_time);
            if(ifStarved!=null){
                return ifStarved;
            }else{
                Minburst_time=shortestRemainingTimeProcess.getBurstTime();
                if (Minburst_time<min_time) {
                    min_time = Minburst_time;
                    FinaltoStartProcess = shortestRemainingTimeProcess;
                }
            }

        }
        return FinaltoStartProcess;
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
