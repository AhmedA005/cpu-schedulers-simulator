import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuGUI extends JFrame {
    private JTextField contextSwitchField, numProcessesField;
    private List<Processes.Process> processList;

    public MenuGUI() {
        // Initialize the process list
        processList = new ArrayList<>();

        // Frame Configuration
        setTitle("CPU Scheduler Simulator - Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panels for Layout
        JPanel inputPanel = createInputPanel();
        JPanel controlPanel = createControlPanel();

        // Add Panels to Frame
        add(inputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public MenuGUI(List<Processes.Process> processList) {
        this.processList = processList;

        // Frame Configuration
        setTitle("CPU Scheduler Simulator - Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panels for Layout
        JPanel inputPanel = createInputPanel();
        JPanel controlPanel = createControlPanel();

        // Add Panels to Frame
        add(inputPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Program Input"));

        // Input Fields
        panel.add(new JLabel("Number of Processes:"));
        numProcessesField = new JTextField();
        panel.add(numProcessesField);

        panel.add(new JLabel("Context Switch Time:"));
        contextSwitchField = new JTextField();
        panel.add(contextSwitchField);

        // Add Button to Collect Process Details
        JButton addProcessButton = new JButton("Add Processes");
        addProcessButton.addActionListener(e -> addProcesses());
        panel.add(addProcessButton);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Scheduler Selection Dropdown
        JComboBox<String> schedulerSelector = new JComboBox<>(new String[] {
                "FCAI Scheduler", "SJF Scheduler", "Priority Scheduler", "SRTF Scheduler"
        });
        panel.add(new JLabel("Select Scheduler:"));
        panel.add(schedulerSelector);

        // Run Button
        JButton runButton = new JButton("Run Scheduler");
        runButton.addActionListener(e -> {
            String selectedScheduler = (String) schedulerSelector.getSelectedItem();
            runSelectedScheduler(selectedScheduler);
        });
        panel.add(runButton);

        // Clear Processes Button
        JButton clearButton = new JButton("Clear Processes");
        clearButton.addActionListener(e -> {
            processList.clear();
            JOptionPane.showMessageDialog(this, "Process list cleared!");
        });
        panel.add(clearButton);

        return panel;
    }


    private void addProcesses() {
        try {
            int numProcesses = Integer.parseInt(numProcessesField.getText());

            for (int i = 0; i < numProcesses; i++) {
                // Open a dialog for process details
                JPanel processPanel = new JPanel();
                processPanel.setLayout(new GridLayout(7, 2, 10, 10));

                JTextField nameField = new JTextField();
                JTextField colorField = new JTextField();
                JTextField arrivalField = new JTextField();
                JTextField burstField = new JTextField();
                JTextField priorityField = new JTextField();
                JTextField quantumField = new JTextField();

                processPanel.add(new JLabel("Process Name:"));
                processPanel.add(nameField);

                processPanel.add(new JLabel("Process Color:"));
                processPanel.add(colorField);

                processPanel.add(new JLabel("Arrival Time:"));
                processPanel.add(arrivalField);

                processPanel.add(new JLabel("Burst Time:"));
                processPanel.add(burstField);

                processPanel.add(new JLabel("Priority:"));
                processPanel.add(priorityField);

                processPanel.add(new JLabel("Quantum:"));
                processPanel.add(quantumField);

                int result = JOptionPane.showConfirmDialog(
                        this, processPanel, "Enter Process Details for Process " + (i + 1),
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    // Add the process to the list
                    String name = nameField.getText();
                    String color = colorField.getText();
                    int arrival = Integer.parseInt(arrivalField.getText());
                    int burst = Integer.parseInt(burstField.getText());
                    int priority = Integer.parseInt(priorityField.getText());
                    int quantum = Integer.parseInt(quantumField.getText());

                    processList.add(new Processes.Process(name, arrival, burst, priority, quantum, color));
                } else {
                    break; // Stop adding processes if the user cancels
                }
            }

            JOptionPane.showMessageDialog(this, "Processes added successfully!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runSelectedScheduler(String selectedScheduler) {
        try {
            int contextSwitchTime = Integer.parseInt(contextSwitchField.getText());

            switch (selectedScheduler) {
                case "FCAI Scheduler":
                    FCAIGUI fcaiGUI = new FCAIGUI(processList);
                    fcaiGUI.setVisible(true);
                    break;

                case "SJF Scheduler":
                    SJFGUI sjfGUI = new SJFGUI(processList);
                    sjfGUI.setVisible(true);
                    break;

                case "Priority Scheduler":
                    PriorityGUI priorityGUI = new PriorityGUI(processList, contextSwitchTime);
                    priorityGUI.setVisible(true);
                    break;

                case "SRTF Scheduler":
                    SRTFGUI srtfGUI = new SRTFGUI(processList, contextSwitchTime);
                    srtfGUI.setVisible(true);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Invalid scheduler selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close Menu GUI
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Context Switch Time.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuGUI menuGUI = new MenuGUI();
            menuGUI.setVisible(true);
        });
    }
}
