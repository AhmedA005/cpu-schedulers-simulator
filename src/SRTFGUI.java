import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Processes.Process;
import Processes.ShortestRemainingTimeProcess;
import Schedulers.ShortestRemainingTimeFirst;

import static java.lang.Math.ceil;

public class SRTFGUI extends JFrame {
    private JTextArea outputArea;
    private JPanel ganttChartPanel;
    List<Processes.Process> processList;

    public SRTFGUI(List<Processes.Process> processList, int contextSwitchTime) {
        this.processList = processList;
        setTitle("Shortest Remaining Time Scheduler Simulator");
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
            // Convert the general Process list to ShortestRemainingTimeProcess list
            List<Process> srtfProcessList = new ArrayList<>();
            for (Processes.Process p : processList) {
                ShortestRemainingTimeProcess srtfProcess = new ShortestRemainingTimeProcess(
                        p.getName(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getPriority(),
                        p.getColor()
                );
                srtfProcessList.add(srtfProcess);
            }

            // Run the SRTF scheduler with the casted list
            ShortestRemainingTimeFirst scheduler = new ShortestRemainingTimeFirst(srtfProcessList, contextSwitchTime);
            scheduler.run();

            double totalWaitingTime = 0;
            double totalTurnaroundTime = 0;

            outputArea.append("\nScheduling Results:\n");
            for (ShortestRemainingTimeProcess pp : scheduler.tempList) {
                outputArea.append("Process: " + pp.getName() +
                        ", Waiting Time: " + pp.getWaitingTime() +
                        ", Turnaround Time: " + pp.get_TurnaroundTime() + "\n");
                totalWaitingTime += pp.getWaitingTime();
                totalTurnaroundTime += pp.get_TurnaroundTime();
            }

            double averageWaitingTime = Math.ceil(totalWaitingTime / scheduler.tempList.size());
            double averageTurnaroundTime = Math.ceil(totalTurnaroundTime / scheduler.tempList.size());
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

        int currentY = 20;
        int currentX = 10;
        int scaleFactor = 20;
        int lineHeight = 50;
        int panelWidth = 800;

        for (ShortestRemainingTimeProcess process : executionList) {
            int width = process.get_originalBurstTime() * scaleFactor;

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

            label.setBounds(currentX, currentY, width, 30);

            ganttChartPanel.add(label);
            currentX += width + 5;

            if (currentX > panelWidth) {
                currentX = 10;
                currentY += lineHeight + 10;
            }
        }

        int totalHeight = currentY + lineHeight;
        ganttChartPanel.setPreferredSize(new Dimension(panelWidth, totalHeight));

        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }

    public static void main(String[] args) {
        // Test with dummy processes
        List<Processes.Process> testProcesses = List.of(
                new ShortestRemainingTimeProcess("P1", 0, 7, 1, "red"),
                new ShortestRemainingTimeProcess("P2", 2, 4, 2, "blue"),
                new ShortestRemainingTimeProcess("P3", 4, 1, 3, "yellow"),
                new ShortestRemainingTimeProcess("P4", 5, 4, 4, "green")
        );

        SwingUtilities.invokeLater(() -> {
            SRTFGUI gui = new SRTFGUI(testProcesses, 1);
            gui.setVisible(true);
        });
    }
}
