import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Processes.FCAIProcess;
import Schedulers.FCAIScheduling;

public class FCAIGUI extends JFrame {
    private JTextField nameField, priorityField, arrivalField, burstField, quantumField, colorField;
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    private List<Processes.Process> processList;

    public FCAIGUI() {
        setTitle("FCAI Scheduler Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize processList
        processList = new ArrayList<>();

        // Create panels
        JPanel inputPanel = createInputPanel();
        JPanel outputPanel = createOutputPanel();
        JPanel controlPanel = createControlPanel();

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Section"));

        // Process Name
        inputPanel.add(new JLabel("Process Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        // Arrival Time
        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        // Burst Time
        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        // Priority
        inputPanel.add(new JLabel("Priority:"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        // Initial Quantum
        inputPanel.add(new JLabel("Quantum:"));
        quantumField = new JTextField();
        inputPanel.add(quantumField);

        // Process Color
        inputPanel.add(new JLabel("Process Color:"));
        colorField = new JTextField();
        inputPanel.add(colorField);

        // Add Process Button
        JButton addButton = new JButton("Add Process");
        addButton.addActionListener(e -> addProcess());
        inputPanel.add(addButton);

        return inputPanel;
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

        JButton runButton = new JButton("Run Scheduler");
        runButton.addActionListener(e -> runScheduler());
        panel.add(runButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearData());
        panel.add(clearButton);

        return panel;
    }

    private void addProcess() {
        try {
            // Read inputs from GUI fields
            String name = nameField.getText();
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            int priority = Integer.parseInt(priorityField.getText());
            int quantum = Integer.parseInt(quantumField.getText());
            String color = colorField.getText();

            // Validate inputs
            if (arrivalTime < 0 || burstTime <= 0 || priority < 0 || quantum <= 0) {
                throw new IllegalArgumentException("All numeric values must be positive.");
            }

            // Create FCAIProcess and add to processList
            FCAIProcess process = new FCAIProcess(name, arrivalTime, burstTime, priority, quantum, color);
            processList.add(process);

            // Update outputArea with process details
            outputArea.append("Added Process: " + name + "\n");
            clearInputFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding process: " + ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScheduler() {
        try {
            // Always use FCAIScheduling
            FCAIScheduling scheduler = new FCAIScheduling(processList);
            scheduler.run();

            outputArea.append("\nFCAI Scheduling Results:\n");
            for (Processes.Process p : scheduler.finishedProcesses) {
                FCAIProcess fp = (FCAIProcess) p;
                outputArea.append("Process: " + fp.getName() +
                        ", Waiting Time: " + (fp.getFinalFinishTime() - fp.getArrivalTime() - fp.getBurstTime()) +
                        ", Turnaround Time: " + (fp.getFinalFinishTime() - fp.getArrivalTime()) +
                        ", Quantum History: " + fp.getQuantumHistory() + "\n");
            }

            drawGanttChart(scheduler.finishedProcesses);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawGanttChart(List<Processes.Process> processes) { //same row
        ganttChartPanel.removeAll(); // Remove previous drawings
        ganttChartPanel.setLayout(null); // Absolute positioning for custom placement

        int currentY = 20; // Start position for the first process (vertical position)
        int currentX = 10; // Start position for the first process (horizontal position)
        int scaleFactor = 20; // Scale factor to adjust the width of each process based on burst time
        int lineHeight = 50; // Height of each line (row) where a process will be placed
        int panelWidth = 800; // Width of the panel (you can adjust this as needed)

        // Loop through the processes and place them on the panel
        for (Processes.Process process : processes) {
            FCAIProcess pp = (FCAIProcess) process;

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
                case "white": label.setBackground(Color.WHITE); break;
                default: label.setBackground(Color.GRAY); break;
            }

            label.setBounds(currentX, currentY, width, 30); // Set position

            ganttChartPanel.add(label); // Add the label to the panel

            // Move to the next row
            currentX = 10; // Reset X to start from the left again
            currentY += lineHeight + 10; // Move to the next row (next Y position)
        }

        // Dynamically adjust the panel height based on the number of rows
        int totalHeight = currentY + lineHeight; // Calculate the height of the panel based on the processes
        ganttChartPanel.setPreferredSize(new Dimension(panelWidth, totalHeight)); // Set the height dynamically

        // Revalidate and repaint to apply changes
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }

    /*private void drawGanttChart(List<Processes.Process> processes) { Separate rows
        ganttChartPanel.removeAll(); // Remove previous drawings
        ganttChartPanel.setLayout(null); // Absolute positioning for custom placement

        int currentY = 20; // Start position for the first process (vertical position)
        int currentX = 10; // Start position for the first process (horizontal position)
        int scaleFactor = 20; // Scale factor to adjust the width of each process based on burst time
        int lineHeight = 50; // Height of each line (row) where a process will be placed
        int panelWidth = 800; // Width of the panel (you can adjust this as needed)

        // Loop through the processes and place them on the panel
        for (Processes.Process process : processes) {
            FCAIProcess pp = (FCAIProcess) process;

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
                case "white": label.setBackground(Color.WHITE); break;
                default: label.setBackground(Color.GRAY); break;
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
    }*/

    private void clearData() {
        processList.clear();
        outputArea.setText("");
        ganttChartPanel.removeAll();
        ganttChartPanel.repaint();
    }

    private void clearInputFields() {
        nameField.setText("");
        priorityField.setText("");
        arrivalField.setText("");
        burstField.setText("");
        quantumField.setText("");
        colorField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FCAIGUI gui = new FCAIGUI();
            gui.setVisible(true);
        });
    }
}
