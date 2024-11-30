package Schedulers;

import Processes.FCAIProcess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FCAIScheduling extends Scheduler {
    List<FCAIProcess> finishedProcesses;
    List<FCAIProcess> readyQueue;
    int currentTime;

    public FCAIScheduling(List<FCAIProcess> processList) {
        super(processList);
        this.finishedProcesses = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        this.currentTime = 0;
    }

    @Override
    public void run() {
        processList.sort(Comparator.comparing(FCAIProcess::getArrivalTime));

        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            addArrivedProcesses();

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            FCAIProcess currentProcess = readyQueue.removeFirst();
            readyQueue.sort(Comparator.comparing(this::calculateFCAIFactor));

            executeProcess(currentProcess);
        }
        calculateAndPrint(finishedProcesses);
    }

    @Override
    public void calculateAndPrint(List<FCAIProcess> processList) {
        System.out.println("FCAI Scheduling Results:");
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (FCAIProcess process : processList) {
            int waitingTime = process.getLastFinishTime() - process.getArrivalTime() - process.getBurstTime();
            int turnaroundTime = process.getLastFinishTime() - process.getArrivalTime();

            System.out.println("Process: " + process.getName());
            System.out.println("Waiting Time: " + waitingTime);
            System.out.println("Turnaround Time: " + turnaroundTime);
            System.out.println("Quantum History: " + process.getQuantumHistory());
            System.out.println();

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

        }

        System.out.println("Average Waiting Time: " + totalWaitingTime / processList.size());
        System.out.println("Average Turnaround Time: " + totalTurnaroundTime / processList.size());
    }

    private void executeProcess(FCAIProcess currentProcess) {
        int nonPreemptiveTime = (int) Math.ceil(0.4 * currentProcess.getQuantum());
        int execTime = Math.min(currentProcess.getRemainingBurstTime(), currentProcess.getQuantum());

        // Execute the process, checking for preemption at each step
        for (int executedTime = 0; executedTime < execTime; ) {
            // Check if we're past the non-preemptive period
            boolean isPastNonPreemptivePeriod = executedTime >= nonPreemptiveTime;

            // Check if there's a better process in the queue
            boolean shouldPreempt = !readyQueue.isEmpty() &&
                    isPastNonPreemptivePeriod &&
                    calculateFCAIFactor(readyQueue.get(0)) <= calculateFCAIFactor(currentProcess);

            if (shouldPreempt) {
                // Preempt the current process
                currentProcess.setPreempted(true);
                updateProcessQuantum(currentProcess);
                readyQueue.add(currentProcess);
                return;
            }

            // Execute for 1 time unit
            currentProcess.execute(1);
            currentTime++;
            executedTime++;

            // Break if process is complete
            if (currentProcess.getRemainingBurstTime() <= 0) {
                finishedProcesses.add(currentProcess);
                return;
            }
            addArrivedProcesses();

        }

        // Update quantum and handle process disposition
        updateProcessQuantum(currentProcess);

        if (currentProcess.getRemainingBurstTime() > 0) {
            readyQueue.add(currentProcess);
        } else {
            finishedProcesses.add(currentProcess);
        }
    }

    private void updateProcessQuantum(FCAIProcess currentProcess) {
        if (currentProcess.getRemainingBurstTime() > 0) {
            if (currentProcess.isPreempted()) {
                currentProcess.setQuantum(currentProcess.getQuantum() + (currentProcess.getQuantum() - (currentProcess.getLastBurstTime() - currentProcess.getRemainingBurstTime())));
            } else currentProcess.setQuantum(currentProcess.getQuantum() + 2);
        }

    }

    private void addArrivedProcesses() {
        Iterator<FCAIProcess> iterator = processList.iterator();
        while (iterator.hasNext()) {
            FCAIProcess process = iterator.next();
            if (process.getArrivalTime() <= currentTime) {
                readyQueue.add(process);
                iterator.remove();
            }
        }
    }


    private double calculateFCAIFactor(FCAIProcess process) {
        double V1 = processList.stream().mapToDouble(FCAIProcess::getArrivalTime).max().orElse(0) / 10;
        double V2 = processList.stream().mapToDouble(FCAIProcess::getBurstTime).max().orElse(0) / 10;
        return (10 - process.getPriority()) + ((double) process.getArrivalTime() / V1)
                + ((double) process.getRemainingBurstTime() / V2);
    }
}
