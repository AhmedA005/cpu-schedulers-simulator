import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Processes.Process;
import Processes.FCAIProcess;
import Schedulers.FCAIScheduling;


public class FCAIGUI extends JFrame {
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    List<Processes.Process> processList;

    public FCAIGUI(List<Processes.Process> processList) {
        this.processList = processList;
        setTitle("FCAI Scheduler Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create panels
        JPanel outputPanel = createOutputPanel();
        JPanel controlPanel = createControlPanel();

        // Add panels to frame
        add(outputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Run the scheduler directly
        runScheduler(processList);
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Output Section"));

        // Text Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Gantt Chart Panel
        ganttChartPanel = new JPanel();
        ganttChartPanel.setBackground(Color.LIGHT_GRAY);
        ganttChartPanel.setLayout(null); // Absolute positioning for custom placement

        // Wrap the ganttChartPanel in a JScrollPane once
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
        ganttScrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(ganttScrollPane, BorderLayout.SOUTH); // Add scrollable Gantt chart
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton returnButton = new JButton("Return to Menu");
        returnButton.addActionListener(e -> {
            // Return to MenuGUI with the current process list
            new MenuGUI(processList).setVisible(true);
            dispose(); // Close the current scheduler GUI
        });
        panel.add(returnButton);

        return panel;
    }


    private void runScheduler(List<Processes.Process> processList) {
        try {
            // Convert the general Process list to FCAIProcess list
            List<Process> fcaiProcessList = new ArrayList<>();
            for (Processes.Process p : processList) {
                FCAIProcess fcaiProcess = new FCAIProcess(
                        p.getName(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getPriority(),
                        p.getQuantum(),
                        p.getColor()
                );
                fcaiProcessList.add(fcaiProcess);
            }

            // Run the FCAI scheduler with the casted list
            FCAIScheduling scheduler = new FCAIScheduling(fcaiProcessList);
            scheduler.run();

            outputArea.append("\nFCAI Scheduling Results:\n");
            for (Processes.Process p : scheduler.getFinishedProcesses()) {
                FCAIProcess fp = (FCAIProcess) p;
                outputArea.append("Process: " + fp.getName() +
                        ", Waiting Time: " + (fp.getFinalFinishTime() - fp.getArrivalTime() - fp.getBurstTime()) +
                        ", Turnaround Time: " + (fp.getFinalFinishTime() - fp.getArrivalTime()) +
                        ", Quantum History: " + fp.getQuantumHistory() + "\n");
            }
            double averageWaitingTime = scheduler.getAverageWaitingTime();
            double averageTurnAroundTime = scheduler.getAverageTurnAroundTime();
            outputArea.append("\nAverage Waiting Time: " + averageWaitingTime + "\n");
            outputArea.append("Average Turnaround Time: " + averageTurnAroundTime + "\n");

            updateGanttChart(scheduler.getProcessExeutionOrder(), scheduler.getRemaining());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGanttChart(List<FCAIProcess> executionOrder, List<Integer> remaining) {
        ganttChartPanel.removeAll();
        int currentX = 10;
        int currentY = 20; // Fixed Y position
        int scaleFactor = 15;
        int lineHeight = 30; // Height of each row
        int counter = 0;
        int panelWidth = 800; // Width of the panel (you can adjust this as needed)

        for (FCAIProcess pp : executionOrder) {
            // Calculate width based on burst time (scaled by scaleFactor)
            int width = (pp.getBurstTime() - remaining.get(counter)) * scaleFactor;
            counter++;
            // Create a label for the process
            JLabel label = new JLabel(pp.getName(), SwingConstants.CENTER);
            label.setOpaque(true);

            // Set background color based on process color
            switch (pp.getColor().toLowerCase()) {
                case "red":
                    label.setBackground(Color.RED);
                    break;
                case "blue":
                    label.setBackground(Color.BLUE);
                    break;
                case "yellow":
                    label.setBackground(Color.YELLOW);
                    break;
                case "green":
                    label.setBackground(Color.GREEN);
                    break;
                case "cyan":
                    label.setBackground(Color.CYAN);
                    break;
                case "pink":
                    label.setBackground(Color.MAGENTA);
                    break;
                case "white":
                    label.setBackground(Color.WHITE);
                    break;
                default:
                    label.setBackground(Color.ORANGE);
                    break;
            }

            label.setBounds(currentX, currentY, width, lineHeight); // Set position

            ganttChartPanel.add(label); // Add the label to the panel
            currentX += width + 5;
            // Move to the next position horizontally
            if (currentX > panelWidth) {
                currentX = 10; // Reset X to start from the left again
                currentY += lineHeight + 10; // Move to the next row (next Y position)
            }
        }

        // Dynamically adjust the panel height based on the number of rows
        int totalHeight = currentY + lineHeight; // Calculate the height of the panel based on the processes
        ganttChartPanel.setPreferredSize(new Dimension(panelWidth, totalHeight));

        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }
}

