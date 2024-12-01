package Schedulers;

import Processes.FCAIProcess;
import Processes.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FCAIScheduling extends Scheduler {
    List<Process> finishedProcesses;
    List<FCAIProcess> readyQueue;
    List<Process> copyProcessList;
    int currentTime;
    double V1;
    double V2;

    public FCAIScheduling(List<Process> processList) {
        super(processList);
        this.copyProcessList = new ArrayList<>(processList);
        this.finishedProcesses = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        this.currentTime = 0;
        this.V1 = processList.stream().mapToDouble(Process::getArrivalTime).max().orElse(0) / 10;
        this.V2 = processList.stream().mapToDouble(Process::getBurstTime).max().orElse(0) / 10;
    }

    @Override
    public void run() {
        processList.sort(Comparator.comparing(Process::getArrivalTime));

        while (!copyProcessList.isEmpty() || !readyQueue.isEmpty()) {
            addArrivedProcesses();

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            FCAIProcess currentProcess = readyQueue.removeFirst();

            executeProcess(currentProcess);
        }
        calculateAndPrint(finishedProcesses);
    }

    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {
        System.out.println("FCAI Scheduling Results:");
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;


        for (Process process : finishedProcesses) {
            int waitingTime = (((FCAIProcess) process).getFinalFinishTime()) - process.getArrivalTime() - process.getBurstTime();
            int turnaroundTime = ((FCAIProcess) process).getFinalFinishTime() - process.getArrivalTime();

            System.out.println("Process: " + process.getName());
            System.out.println("Waiting Time: " + waitingTime);
            System.out.println("Turnaround Time: " + turnaroundTime);
            System.out.println("Quantum History: " + ((FCAIProcess) process).getQuantumHistory());
            System.out.println();

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

        }


        System.out.println("Average Waiting Time: " + totalWaitingTime / finishedProcesses.size());
        System.out.println("Average Turnaround Time: " + totalTurnaroundTime / finishedProcesses.size());
    }

    private void executeProcess(FCAIProcess currentProcess) {
        int nonPreemptiveTime = (int) Math.ceil(0.4 * currentProcess.getQuantum());
        int execTime = Math.min(currentProcess.getRemainingBurstTime(), currentProcess.getQuantum());
        int executedTime = 0;

        // Execute the process, checking for preemption at each step
        while (executedTime < execTime) {
            // Check if we're past the non-preemptive period
            boolean isPastNonPreemptivePeriod = executedTime >= nonPreemptiveTime;

            // Check if there's a better process in the queue
            FCAIProcess betterProcess = checkBetterProcess(currentProcess);
            boolean shouldPreempt = !readyQueue.isEmpty() &&
                    isPastNonPreemptivePeriod &&
                    betterProcess != null;

            if (shouldPreempt) {
                // Preempt the current process
                currentProcess.setPreempted(true);
                readyQueue.remove(betterProcess);
                readyQueue.addFirst(betterProcess);
                updateProcessQuantum(currentProcess, executedTime);
                readyQueue.add(currentProcess);
                return;
            }

            // Execute for 1 time unit
            currentProcess.execute(1);
            currentTime++;
            executedTime++;

            // Break if process is complete
            if (currentProcess.getRemainingBurstTime() <= 0) {
                currentProcess.setPreempted(false);
                currentProcess.setFinalFinishTime(currentTime);
                finishedProcesses.add(currentProcess);
                return;
            }
            addArrivedProcesses();

        }

        currentProcess.setPreempted(false);
        // Update quantum and handle process disposition
        updateProcessQuantum(currentProcess, executedTime);

        if (currentProcess.getRemainingBurstTime() > 0) {
            readyQueue.add(currentProcess);
        } else {
            finishedProcesses.add(currentProcess);
        }
    }

    private void updateProcessQuantum(FCAIProcess currentProcess, int executedTime) {
        if (currentProcess.getRemainingBurstTime() > 0) {
            if (currentProcess.isPreempted()) {
                currentProcess.setQuantum(currentProcess.getQuantum() + (currentProcess.getQuantum() - executedTime));
            } else currentProcess.setQuantum(currentProcess.getQuantum() + 2);
        }

    }

    private void addArrivedProcesses() {
        Iterator<Process> iterator = copyProcessList.iterator();
        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (process.getArrivalTime() <= currentTime) {
                readyQueue.add((FCAIProcess) process);

                iterator.remove();
            }
        }
    }


    private double calculateFCAIFactor(FCAIProcess process) {
        return (10 - process.getPriority()) + ((double) process.getArrivalTime() / V1)
                + ((double) process.getRemainingBurstTime() / V2);
    }

    private FCAIProcess checkBetterProcess(FCAIProcess currentProcess) {
        FCAIProcess bestProcess = null;
        for (FCAIProcess process : readyQueue) {
            if (process != currentProcess && calculateFCAIFactor(process) < calculateFCAIFactor(currentProcess)) {
                bestProcess = process;
                currentProcess = process;
            }
        }
        return bestProcess;
    }
}
