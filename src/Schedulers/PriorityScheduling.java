package Schedulers;

import Processes.PriorityProcess;

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

public class PriorityScheduling{
    private List<PriorityProcess> processList;
    public PriorityScheduling(List<PriorityProcess> ProcessList) {
        this.processList = ProcessList;
    }
    int time = 0;
    int size = processList.size();
    int counter = 0;
    float avgwaitingTime = 0;
    public void run() {
        processList.sort(Comparator.comparingInt(PriorityProcess::getPriority));
        while(counter < size) {
            boolean flag = false;
            for (PriorityProcess p : processList) {
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
        System.out.println("Average waiting time: " + avgwaitingTime/size);
    }
}
