import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import Processes.Process;
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
            for (Processes.Process p : scheduler.getFinishedProcesses()) {
                FCAIProcess fp = (FCAIProcess) p;
                outputArea.append("Process: " + fp.getName() +
                        ", Waiting Time: " + (fp.getFinalFinishTime() - fp.getArrivalTime() - fp.getBurstTime()) +
                        ", Turnaround Time: " + (fp.getFinalFinishTime() - fp.getArrivalTime()) +
                        ", Quantum History: " + fp.getQuantumHistory() + "\n");
            }

            updateGanttChart(scheduler.getProcessExeutionOrder());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGanttChart(List<Process> executionOrder) {
        ganttChartPanel.removeAll();
        int currentX = 10;
        int currentY = 20; // Fixed Y position
        int scaleFactor = 5;
        int lineHeight = 30; // Height of each row

        for (Process pp : executionOrder) {
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

            label.setBounds(currentX, currentY, width, lineHeight); // Set position

            ganttChartPanel.add(label); // Add the label to the panel

            // Move to the next position horizontally
            currentX += width + 10; // Increment X position by width + some spacing
        }

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
