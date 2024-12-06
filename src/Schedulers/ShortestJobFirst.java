package Schedulers;

import Processes.Process;
import Processes.ShortestJobFirstProcess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;

public class ShortestJobFirst extends Scheduler {
    private int currentTime = 0;

    private List<ShortestJobFirstProcess> finishedProcesses = new ArrayList<>();

    public ShortestJobFirst(List<Process> ProcessList) {
        super(ProcessList);
    }
/*    public List<Process> sorting() {
        for (Process p : processList) {
            arrivalTimes.add(new ShortestJobFirstProcess(p.getName(),p.getArrivalTime(),p.getBurstTime(),p.getPriority(),p.getColor()));
            burstTimes.add(new ShortestJobFirstProcess(p.getName(),p.getArrivalTime(),p.getBurstTime(),p.getPriority(),p.getColor()));
        }
        arrivalTimes.sort(Comparator.comparingInt(Process::getArrivalTime));
        burstTimes.sort(Comparator.comparingInt(Process::getBurstTime));
        System.out.println("By Arrival Times:");
        for (Process p : arrivalTimes) {
            System.out.println(p.getName() + " " + p.getArrivalTime() + " " + p.getBurstTime());
        }
        System.out.println("By Burst Times:");
        for (Process p : burstTimes) {
            System.out.println(p.getName() + " " + p.getArrivalTime() + " " + p.getBurstTime());
        }
        return arrivalTimes;
    }*/


    @Override
    public void run() {
        while (!processList.isEmpty()) {
        List<Process> arrivedProcesses = new ArrayList<>();
        for (Process p : processList) {
            if (p.getArrivalTime() <= currentTime) {
            arrivedProcesses.add(new ShortestJobFirstProcess(p.getName(),p.getArrivalTime(),p.getBurstTime(),p.getPriority(),p.getColor()));
            }
        }
        arrivedProcesses.sort(Comparator.comparingInt(Process::getBurstTime));
        List<Process> temp = new ArrayList<>();
        temp.add(arrivedProcesses.getFirst());
        for (int i = 0; i < arrivedProcesses.size()-1; i++) {
            if(arrivedProcesses.get(i).getBurstTime() == arrivedProcesses.get(i+1).getBurstTime()) {
                temp.add(arrivedProcesses.get(i+1));
            }
            else break;
        }
        temp.sort(Comparator.comparingInt(Process::getArrivalTime));
        ShortestJobFirstProcess p = (ShortestJobFirstProcess) temp.getFirst();
        p.setWaitingTime(currentTime - p.getArrivalTime());
        p.setTurnAroundTime(p.getWaitingTime() + p.getBurstTime());
        currentTime += p.getBurstTime();
        finishedProcesses.add(p);
        String processName = p.getName();
        for (Process p2 : processList) {
            if (p2.getName() == processName) {
                processList.remove(p2);
                break;
            }
        }
        System.out.println(p.getName() + " " + p.getArrivalTime() + " " + p.getBurstTime());
        }
    }


    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {
        double totalWaitingTime = 0;
        double totalTurnAroundTime = 0;
        int count = 0;
        System.out.println("Process #  " + " Name " +" Waiting Time " + " Turn Around Time ");
        for (ShortestJobFirstProcess p : finishedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnAroundTime += p.getTurnAroundTime();
            System.out.println(count + "\t\t\t" +p.getName() + "\t\t\t" + p.getWaitingTime() + "\t\t\t" + p.getTurnAroundTime());
            count++;
        }
        double averageWaitingTime = totalWaitingTime / finishedProcesses.size();
        double averageTurnAroundTime = totalTurnAroundTime / finishedProcesses.size();
        System.out.println("Average waiting time : " + averageWaitingTime);
        System.out.println("Average turn around time : " + averageTurnAroundTime);
    }
    public void cap(){
        calculateAndPrint(processList);
    }
}
