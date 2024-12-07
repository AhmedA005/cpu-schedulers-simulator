import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import Schedulers.PriorityScheduling;
import Processes.PriorityProcess;

public class PriorityGUI extends JFrame {
    private JTextField nameField, priorityField, arrivalField, burstField, colorField, contextSwitchField;
    private JComboBox<String> schedulerSelector;
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    private List<Processes.Process> processList;

    public PriorityGUI() {
        // Initialize Process List
        processList = new ArrayList<>();

        // Frame Configurationz
        setTitle("CPU Scheduler Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panels for Layout
        JPanel inputPanel = createInputPanel();
        JPanel outputPanel = createOutputPanel();
        JPanel controlPanel = createControlPanel();

        // Add Panels to Frame
        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Input Section"));

        // Input Fields
        panel.add(new JLabel("Process Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Priority:"));
        priorityField = new JTextField();
        panel.add(priorityField);

        panel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        panel.add(arrivalField);

        panel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        panel.add(burstField);

        panel.add(new JLabel("Process Color:"));
        colorField = new JTextField();
        panel.add(colorField);

        panel.add(new JLabel("Context Switch Time:"));
        contextSwitchField = new JTextField();
        panel.add(contextSwitchField);

        panel.add(new JLabel("Scheduler Type:"));
        schedulerSelector = new JComboBox<>(new String[]{"Priority", "SJF", "SRTF", "FCAI"});
        panel.add(schedulerSelector);

        JButton addButton = new JButton("Add Process");
        addButton.addActionListener(e -> addProcess());
        panel.add(addButton);

        return panel;
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

        // Wrap the ganttChartPanel in a JScrollPane once
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
        ganttScrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(ganttScrollPane, BorderLayout.SOUTH); // Add scrollable Gantt chart
        return panel;
    }


    /*private JPanel createOutputPanel() {
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
        ganttChartPanel.setLayout(null); // Required for manual positioning in the Gantt chart

        // Add the Gantt chart to a scrollable pane
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
        ganttScrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(ganttScrollPane, BorderLayout.SOUTH); // Add scrollable Gantt chart
        return panel;
    }*/


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
            String name = nameField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            String color = colorField.getText();

            PriorityProcess process = new PriorityProcess(name, arrivalTime, burstTime, priority, color);
            processList.add(process);

            outputArea.append("Added Process: " + name + " (Priority: " + priority + ", Arrival: " + arrivalTime +
                    ", Burst: " + burstTime + ", Color: " + color + ")\n");

            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScheduler() {
        try {
            float contextSwitchTime = Float.parseFloat(contextSwitchField.getText());
            String schedulerType = (String) schedulerSelector.getSelectedItem();

            // Instantiate the scheduler based on selection
            PriorityScheduling scheduler = new PriorityScheduling(processList, contextSwitchTime);
            scheduler.run();

            // Output the results in the TextArea
            outputArea.append("\nScheduling Results:\n");
            for (Processes.Process p : scheduler.finishedProcesses) {
                PriorityProcess pp = (PriorityProcess) p;
                outputArea.append("Process: " + pp.getName() +
                        ", Waiting Time: " + pp.getTotalWaitingTime() +
                        ", Turnaround Time: " + pp.getTurnaroundTime() + "\n");
            }

            outputArea.append("Average Waiting Time: " + scheduler.avgWaitingTime / scheduler.size + "\n");
            outputArea.append("Average Turnaround Time: " + scheduler.avgTurnaroundTime / scheduler.size + "\n");

            // Draw Gantt Chart
            drawGanttChart(scheduler.finishedProcesses);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*private void drawGanttChart(List<Processes.Process> processes) {
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
                case "white": label.setBackground(Color.WHITE); break;
                default: label.setBackground(Color.GRAY); break;
            }

            // Position the process label
            label.setBounds(currentX, currentY, width, 30); // One process per row, width based on burst time

            ganttChartPanel.add(label); // Add the label to the panel

            // Move to the next vertical position (next row)
            currentY += lineHeight + 10; // Move down for the next process
        }

        // Dynamically adjust the panel height based on the number of processes
        int totalHeight = currentY + lineHeight; // Calculate the height of the panel based on the processes
        ganttChartPanel.setPreferredSize(new Dimension(panelWidth, totalHeight)); // Set the height dynamically

        // Revalidate and repaint to apply changes
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }*/

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
}








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
        colorField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PriorityGUI gui = new PriorityGUI();
            gui.setVisible(true);
        });
    }
}
