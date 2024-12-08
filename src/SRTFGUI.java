import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Processes.ShortestRemainingTimeProcess;
import Processes.ShortestJobFirstProcess;
import Schedulers.ShortestRemainingTimeFirst;

import static java.lang.Math.ceil;

public class SRTFGUI extends JFrame {
    private JTextField nameField, priorityField, arrivalField, burstField, colorField, contextSwitching;
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    private List<Processes.Process> processList;

    public SRTFGUI() {
        processList = new ArrayList<>();

        setTitle("Shortest Remaining Time Scheduler Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = createInputPanel();
        JPanel outputPanel = createOutputPanel();
        JPanel controlPanel = createControlPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Input Section"));

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

        panel.add(new JLabel("Context Switching Time:"));
        contextSwitching = new JTextField();
        panel.add(contextSwitching);

        panel.add(new JLabel("Process Color:"));
        colorField = new JTextField();
        panel.add(colorField);

        JButton addButton = new JButton("Add Process");
        addButton.addActionListener(e -> addProcess());
        panel.add(addButton);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Output Section"));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        ganttChartPanel = new JPanel();
        ganttChartPanel.setBackground(Color.LIGHT_GRAY);
        ganttChartPanel.setLayout(null);

        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
        ganttScrollPane.setPreferredSize(new Dimension(800, 200));

        panel.add(ganttScrollPane, BorderLayout.SOUTH);
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
            String name = nameField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            int context= Integer.parseInt(contextSwitching.getText());
            String color = colorField.getText();

            ShortestRemainingTimeProcess process = new ShortestRemainingTimeProcess(name, arrivalTime, burstTime, priority, color,context);
            processList.add(process);

            outputArea.append("Added Process: " + name + " (Priority: " + priority + ", Arrival: " + arrivalTime +
                    ", Burst: " + burstTime + ", Color: " + color + ", context: "+context +")\n");

            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScheduler() {
        try {
            ShortestRemainingTimeFirst scheduler = new ShortestRemainingTimeFirst(processList);
            scheduler.run();

            outputArea.append("\nScheduling Results:\n");
            for (Processes.Process p : scheduler.tempList) {
                ShortestRemainingTimeProcess pp = (ShortestRemainingTimeProcess) p;
                outputArea.append("Process: " + pp.getName() +
                        ", Waiting Time: " + pp.getWaitingTime() +
                        ", Turnaround Time: " + pp.get_TurnaroundTime() + "\n");
            }
            int totalWaitingTime=0;
            int totalTurnaroundTime=0;

            for (ShortestRemainingTimeProcess process : scheduler.tempList) {
                totalWaitingTime += process.getWaitingTime();
                totalTurnaroundTime+=process.get_TurnaroundTime();
            }

            double averageWaitingTime= (int) ceil(totalWaitingTime/scheduler.tempList.size());
            double averageTurnaroundTime=(int) ceil(totalTurnaroundTime/scheduler.tempList.size());
            outputArea.append("\nAverage Waiting Time: " + averageWaitingTime + "\n");
            outputArea.append("Average Turnaround Time: " + averageTurnaroundTime + "\n");

            drawGanttChart(scheduler.executionList);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during scheduling.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void drawGanttChart(List<ShortestRemainingTimeProcess> executionList) {
        ganttChartPanel.removeAll();
        ganttChartPanel.setLayout(null);

        int currentY = 20; // Start position for the first process (vertical position)
        int currentX = 10; // Start position for the first process (horizontal position)
        int scaleFactor = 20; // Scale factor to adjust the width of each process based on burst time
        int lineHeight = 50; // Height of each line (row) where a process will be placed
        int panelWidth = 800; // Width of the panel (you can adjust this as needed)

        for (ShortestRemainingTimeProcess process : executionList) {
            int duration = process.get_originalBurstTime();
            JLabel label = new JLabel(process.getName(), SwingConstants.CENTER);
            label.setOpaque(true);

            switch (process.getColor().toLowerCase()) {
                case "red": label.setBackground(Color.RED); break;
                case "blue": label.setBackground(Color.BLUE); break;
                case "yellow": label.setBackground(Color.YELLOW); break;
                case "green": label.setBackground(Color.GREEN); break;
                case "cyan": label.setBackground(Color.CYAN); break;
                case "pink": label.setBackground(Color.MAGENTA); break;
                case "white": label.setBackground(Color.WHITE); break;
                default: label.setBackground(Color.ORANGE); break;
            }

            label.setBounds(currentX, currentY, duration * scaleFactor, 30);

            ganttChartPanel.add(label);
            currentX += duration * scaleFactor + 5; // Add space between process bars


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
            SRTFGUI gui = new SRTFGUI();
            gui.setVisible(true);
        });
    }
}
