import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Processes.PriorityProcess;
import Processes.Process;
import Schedulers.PriorityScheduling;

public class PriorityGUI extends JFrame {
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    List<Processes.Process> processList;

    public PriorityGUI(List<Processes.Process> processList, int contextSwitchTime) {
        this.processList = processList;
        setTitle("Priority Scheduler Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create output and control panels
        JPanel outputPanel = createOutputPanel();
        JPanel controlPanel = createControlPanel();
        // Add panels to frame
        add(outputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);



        // Run the scheduler directly
        runScheduler(processList, contextSwitchTime);
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
        ganttChartPanel.setLayout(null); // Use absolute positioning for custom placement

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

    private void runScheduler(List<Processes.Process> processList, int contextSwitchTime) {
        try {

            // Convert the general Process list to PriorityProcess list
            List<Process> priorityProcessList = new ArrayList<>();
            for (Processes.Process p : processList) {
                PriorityProcess priorityProcess = new PriorityProcess(
                        p.getName(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getPriority(),
                        p.getColor()
                );
                priorityProcessList.add(priorityProcess);
            }

            // Run the Priority scheduler with the casted list
            PriorityScheduling scheduler = new PriorityScheduling(priorityProcessList, contextSwitchTime);
            scheduler.run();

            outputArea.append("\nScheduling Results:\n");
            for (Processes.Process p : scheduler.finishedProcesses) {
                PriorityProcess pp = (PriorityProcess) p;
                outputArea.append("Process: " + pp.getName() +
                        ", Waiting Time: " + pp.getTotalWaitingTime() +
                        ", Turnaround Time: " + pp.getTurnaroundTime() + "\n");
            }

            outputArea.append("Average Waiting Time: " + scheduler.avgWaitingTime / scheduler.size + "\n");
            outputArea.append("Average Turnaround Time: " + scheduler.avgTurnaroundTime / scheduler.size + "\n");

            drawGanttChart(scheduler.finishedProcesses);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawGanttChart(List<Processes.Process> processes) {
        ganttChartPanel.removeAll(); // Remove previous drawings
        ganttChartPanel.setLayout(null); // Absolute positioning for custom placement

        int currentY = 20; // Start position for the first process (vertical position)
        int currentX = 10; // Start position for the first process (horizontal position)
        int scaleFactor = 20; // Scale factor to adjust the width of each process based on burst time
        int lineHeight = 50; // Height of each line (row) where a process will be placed
        int panelWidth = 800; // Width of the panel (you can adjust this as needed)

        // Loop through the processes and place them on the panel
        for (Processes.Process process : processes) {
            PriorityProcess pp = (PriorityProcess) process;

            // Calculate width based on burst time (scaled by scaleFactor)
            int width = pp.getBurstTime() * scaleFactor;

            // Create a label for the process
            JLabel label = new JLabel(pp.getName(), SwingConstants.CENTER);
            label.setOpaque(true);

            // Set background color based on process color
            switch (pp.getColor().toLowerCase()) {
                case "red": label.setBackground(Color.RED); break;
                case "blue": label.setBackground(Color.BLUE); break;
                case "yellow": label.setBackground(Color.YELLOW); break;
                case "green": label.setBackground(Color.GREEN); break;
                case "cyan": label.setBackground(Color.CYAN); break;
                case "pink": label.setBackground(Color.MAGENTA); break;
                case "white": label.setBackground(Color.WHITE); break;
                default: label.setBackground(Color.ORANGE); break;
            }

            label.setBounds(currentX, currentY, width, 30); // Set position

            ganttChartPanel.add(label); // Add the label to the panel

            // Update the X-coordinate for the next process (next row)
            currentX += width + 5; // Add space between processes

            // If the processes exceed the panel width, move to the next row
            if (currentX > panelWidth) {
                currentX = 10; // Reset X to start from the left again
                currentY += lineHeight + 10; // Move to the next row (next Y position)
            }
        }

        // Dynamically adjust the panel height based on the number of rows
        int totalHeight = currentY + lineHeight; // Calculate the height of the panel based on the processes
        ganttChartPanel.setPreferredSize(new Dimension(panelWidth, totalHeight)); // Set the height dynamically

        // Revalidate and repaint to apply changes
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }

    public static void main(String[] args) {
        // Test with dummy processes
        List<Processes.Process> testProcesses = List.of(
                new PriorityProcess("P1", 0, 8, 1, "red"),
                new PriorityProcess("P2", 1, 5, 3, "blue"),
                new PriorityProcess("P3", 2, 6, 2, "yellow"),
                new PriorityProcess("P4", 3, 4, 4, "green")
        );

        SwingUtilities.invokeLater(() -> {
            PriorityGUI gui = new PriorityGUI(testProcesses, 2); // Example context switch time
            gui.setVisible(true);
        });
    }
}
