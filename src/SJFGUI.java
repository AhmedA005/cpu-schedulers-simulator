import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Processes.Process;
import Processes.ShortestJobFirstProcess;
import Schedulers.ShortestJobFirst;

public class SJFGUI extends JFrame {
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    private List<Processes.Process> processList;

    public SJFGUI(List<Processes.Process> processList) {
        // Assign the process list directly
        this.processList = processList;

        // Frame Configuration
        setTitle("Shortest Job First Scheduler Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allow disposing without exiting
        setLayout(new BorderLayout(10, 10));

        // Panels for Layout
        JPanel outputPanel = createOutputPanel();
        JPanel controlPanel = createControlPanel();

        // Add Panels to Frame
        add(outputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Automatically run the scheduler
        runScheduler();
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

        // Wrap the ganttChartPanel in a JScrollPane
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
        ganttScrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(ganttScrollPane, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Return to Menu Button
        JButton returnButton = new JButton("Return to Menu");
        returnButton.addActionListener(e -> {
            new MenuGUI(processList).setVisible(true); // Return to MenuGUI with the process list
            dispose(); // Close the current GUI
        });
        panel.add(returnButton);

        return panel;
    }

    private void runScheduler() {
        try {
            // Convert the general Process list to ShortestJobFirstProcess list
            List<Process> sjfProcessList = new ArrayList<>();
            for (Processes.Process p : processList) {
                ShortestJobFirstProcess sjfProcess = new ShortestJobFirstProcess(
                        p.getName(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getPriority(),
                        p.getColor()
                );
                sjfProcessList.add(sjfProcess);
            }

            // Run the SJF scheduler with the casted list
            ShortestJobFirst scheduler = new ShortestJobFirst(sjfProcessList);
            scheduler.run();

            outputArea.append("\nScheduling Results:\n");
            for (Processes.Process p : scheduler.finishedProcesses) {
                ShortestJobFirstProcess pp = (ShortestJobFirstProcess) p;
                outputArea.append("Process: " + pp.getName() +
                        ", Waiting Time: " + pp.getWaitingTime() +
                        ", Turnaround Time: " + pp.getTurnAroundTime() + "\n");
            }

            double averageWaitingTime = scheduler.getAverageWaitingTime();
            double averageTurnAroundTime = scheduler.getAverageTurnAroundTime();
            outputArea.append("\nAverage Waiting Time: " + averageWaitingTime + "\n");
            outputArea.append("Average Turnaround Time: " + averageTurnAroundTime + "\n");

            drawGanttChart(scheduler.finishedProcesses);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawGanttChart(List<Process> processes) {
        ganttChartPanel.removeAll(); // Remove previous drawings
        ganttChartPanel.setLayout(null); // Absolute positioning for custom placement

        int currentY = 20; // Start position for the first process (vertical position)
        int currentX = 10; // Start position for the first process (horizontal position)
        int scaleFactor = 20; // Scale factor to adjust the width of each process based on burst time
        int lineHeight = 50; // Height of each line (row) where a process will be placed
        int panelWidth = 800; // Width of the panel (you can adjust this as needed)

        // Loop through the processes and place them on the panel
        for (Processes.Process process : processes) {
            ShortestJobFirstProcess pp = (ShortestJobFirstProcess) process;

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
}
