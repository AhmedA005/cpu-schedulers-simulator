package Schedulers;

import Processes.FCAIProcess;
import Processes.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.ceil;


public class FCAIScheduling extends Scheduler {
    class comp implements Comparator<Process> {

        @Override
        public int compare(Process p1, Process p2) {
            int primary = p1.getArrivalTime() - p2.getArrivalTime();
            if (primary != 0) {
                return primary;
            }
            double factor1 = ceil(calculateFCAIFactor((FCAIProcess) p1));
            double factor2 = ceil(calculateFCAIFactor((FCAIProcess) p2));
            return (int) (factor1 - factor2);
        }
    }

    List<Process> finishedProcesses;
    List<FCAIProcess> readyQueue;
    List<Process> copyProcessList;

    public List<Process> getProcessExeutionOrder() {
        return processExeutionOrder;
    }

    List<Process> processExeutionOrder;
    List<String> executionOrder;
    int currentTime;
    double V1;
    double V2;

    public FCAIScheduling(List<Process> processList) {
        super(processList);
        this.copyProcessList = new ArrayList<>(processList);
        this.finishedProcesses = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        this.executionOrder = new ArrayList<>();
        this.processExeutionOrder = new ArrayList<>();
        this.currentTime = 0;
        this.V1 = processList.stream().mapToDouble(Process::getArrivalTime).max().orElse(0) / 10;
        this.V2 = processList.stream().mapToDouble(Process::getBurstTime).max().orElse(0) / 10;
    }

    public List<Process> getFinishedProcesses() {
        return finishedProcesses;
    }

    @Override
    public void run() {
        copyProcessList.sort(new comp());

        while (!copyProcessList.isEmpty() || !readyQueue.isEmpty()) {
            addArrivedProcesses();

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            FCAIProcess currentProcess = readyQueue.removeFirst();
            processExeutionOrder.add(currentProcess);
            executeProcess(currentProcess);
        }
        calculateAndPrint(finishedProcesses);
    }

    public boolean runScheduler() {
        try {
            copyProcessList.sort(new comp());

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
            return true; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Failure
        }
    }


    @Override
    protected void calculateAndPrint(List<Process> ProcessList) {
        System.out.println("FCAI Scheduling Results:");
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        System.out.println(executionOrder);
        System.out.println();
        finishedProcesses.sort(Comparator.comparing(Process::getArrivalTime));

        for (Process process : finishedProcesses) {

            int waitingTime = (((FCAIProcess) process).getFinalFinishTime()) - process.getArrivalTime() - process.getBurstTime();
            int turnaroundTime = ((FCAIProcess) process).getFinalFinishTime() - process.getArrivalTime();

            System.out.println("Process: " + process.getName());
            System.out.println("Waiting Time: " + waitingTime);
            System.out.println("Turnaround Time: " + turnaroundTime);
            System.out.println("Quantum History: " + ((FCAIProcess) process).getQuantumHistory());

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
            System.out.println();

        }


        System.out.println("Average Waiting Time: " + totalWaitingTime / finishedProcesses.size());
        System.out.println("Average Turnaround Time: " + totalTurnaroundTime / finishedProcesses.size());
    }

    private void executeProcess(FCAIProcess currentProcess) {
        int nonPreemptiveTime = (int) ceil(0.4 * currentProcess.getQuantum());
        int execTime = Math.min(currentProcess.getRemainingBurstTime(), currentProcess.getQuantum());
        int executedTime = 0;
        int startTime = currentTime; // Mark the start time of execution

        executionOrder.add(currentProcess.getName());

        // Execute the process in its quantum
        while (executedTime < execTime) {
            boolean isPastNonPreemptivePeriod = executedTime >= nonPreemptiveTime;

            // Check for preemption
            FCAIProcess betterProcess = checkBetterProcess(currentProcess);
            boolean shouldPreempt = isPastNonPreemptivePeriod &&
                    !readyQueue.isEmpty() &&
                    betterProcess != null;

            if (shouldPreempt) {
                logExecution(startTime, currentTime, currentProcess, executedTime,
                        currentProcess.getName() + " preempted by " + betterProcess.getName());

                currentProcess.setPreempted(true);
                updateProcessQuantum(currentProcess, executedTime);
                readyQueue.add(currentProcess);
                readyQueue.remove(betterProcess);
                readyQueue.addFirst(betterProcess);
                return;
            }

            // Execute for 1 unit of time
            currentProcess.execute(1);
            currentTime++;
            executedTime++;

            // If process completes, log and return
            if (currentProcess.getRemainingBurstTime() <= 0) {
                logExecution(startTime, currentTime, currentProcess, executedTime,
                        currentProcess.getName() + " completes execution");

                currentProcess.setFinalFinishTime(currentTime);
                finishedProcesses.add(currentProcess);
                return;
            }

            addArrivedProcesses();
        }

        // Log the end of quantum with start and end time
        logExecution(startTime, currentTime, currentProcess, executedTime,
                currentProcess.getName() + " completes quantum");

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
        return (10 - process.getPriority()) + ceil(((double) process.getArrivalTime() / V1))
                + ceil(((double) process.getRemainingBurstTime() / V2));
    }

    private FCAIProcess checkBetterProcess(FCAIProcess currentProcess) {
        FCAIProcess bestProcess = null;
        for (FCAIProcess process : readyQueue) {
            if (process != currentProcess && (calculateFCAIFactor(process) < calculateFCAIFactor(currentProcess)
                    || (calculateFCAIFactor(process) == calculateFCAIFactor(currentProcess) && process.getArrivalTime() < currentProcess.getArrivalTime()))) {
                bestProcess = process;
                currentProcess = process;
            }
        }
        return bestProcess;
    }

    private boolean headerPrinted = false;

    private void logExecution(int startTime, int endTime, FCAIProcess currentProcess, int executedTime, String action) {
        double fcaiFactor = ceil(calculateFCAIFactor(currentProcess));

        if (!headerPrinted) {
            System.out.printf("%-8s %-8s %-14s %-18s %-14s %-10s %-12s %-30s\n",
                    "Time", "Process", "Executed Time", "Remaining Burst Time",
                    "Updated Quantum", "Priority", "FCAI Factor", "Action - Details");
            headerPrinted = true;
        }

        // Print process details
        System.out.printf("%-8s %-12s %-14d %-18d %-14d %-10d %-16s %-35s\n",
                startTime + "->" + endTime, currentProcess.getName(), executedTime,
                currentProcess.getRemainingBurstTime(), currentProcess.getQuantum(),
                currentProcess.getPriority(), fcaiFactor, action);
    }
}
