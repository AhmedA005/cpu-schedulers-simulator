package Schedulers;

import Processes.PriorityProcess;
import Processes.Process;

import java.util.Comparator;
import java.util.List;

/*class MyComparator implements Comparator<Process> {
    public int compare(Process x, Process y)
    {

        int comp =  x.getArrivalTime() - y.getArrivalTime();
        if(comp == 0){
            comp =  x.getPriority() - y.getPriority();
        }
        return comp;
    }
}*/

public class PriorityScheduling extends Scheduler {
    private List<Process> processList;
    int size;
    int time;
    int counter;
    float avgwaitingTime;


    public PriorityScheduling(List<Process> processList) {
        super(processList); // Initialize parent class's processList
        this.processList = processList; // Explicitly initialize shadowed field
        this.size = processList.size(); // Dynamically calculate size
        this.time = 0;
        this.counter = 0;
        this.avgwaitingTime = 0;
    }


    public void run() {
        processList.sort(Comparator.comparingInt(Process::getPriority));
        while (counter < size) {
            boolean flag = false;
            for (Process process : processList) {
                PriorityProcess p = (PriorityProcess) process;
                if (p.getArrivalTime() <= time) {
                    flag = true;
                    p.setTotalWaitingTime(time - p.getArrivalTime());
                    avgwaitingTime += p.getTotalWaitingTime();
                    time += p.getBurstTime();
                    //p.setFinished(true);
                    System.out.println("Process: " + p.getName());
                    System.out.println("Waiting time: " + p.getTotalWaitingTime());
                    processList.remove(p);
                    counter++;
                    break;
                }
            }
            if (!flag)
                time++;
        }
        System.out.println("Average waiting time: " + avgwaitingTime / size);
    }

    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {

    }
}
