package hk.edu.polyu.comp.comp2021.tms.view;

import hk.edu.polyu.comp.comp2021.tms.controller.*;
import hk.edu.polyu.comp.comp2021.tms.model.task.CompositeTask;
import hk.edu.polyu.comp.comp2021.tms.model.task.PrimitiveTask;
import hk.edu.polyu.comp.comp2021.tms.model.task.Task;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 800;
    private static final int HORIZONTAL_GAP = 10;
    private static final int VERTICAL_GAP = 10;
    private static final int ROWS1 = 15;
    private static final int COLUMNS1 = 20;
    private static final int COLUMNS2 = 50;

    public static void run() {
        JFrame frame = new JFrame("TMS");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

        JPanel[] userManualPanels = createUserManualPanel();
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
        containerPanel.add(userManualPanels[0]);
        containerPanel.add(userManualPanels[1]);
        containerPanel.add(userManualPanels[2]);
        containerPanel.add(userManualPanels[3]);
        containerPanel.add(userManualPanels[4]);
        containerPanel.add(userManualPanels[5]);

        frame.add(containerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JButton createButton(String buttonText, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(actionListener);
        return button;
    }

    private static JPanel[] createUserManualPanel() {
        JPanel userManualPanel1 = new JPanel();
        userManualPanel1.add(createFileOperationPanel());

        JPanel userManualPanel2 = new JPanel();
        userManualPanel2.add(createTaskOperationPanel());

        JPanel userManualPanel3 = new JPanel();
        userManualPanel3.add(createCriterionOperationPanel());

        JPanel userManualPanel4 = new JPanel();
        userManualPanel4.add(createSearchOperationPanel());

        JPanel userManualPanel5 = new JPanel();
        userManualPanel5.add(createStorageListsOperationPanel());

        JPanel userManualPanel6 = new JPanel();
        userManualPanel6.add(createProgramOperationPanel());

        return new JPanel[] {userManualPanel1, userManualPanel2, userManualPanel3,
                userManualPanel4, userManualPanel5, userManualPanel6};
    }

    private static JPanel createFileOperationPanel() {
        JPanel fileOperationPanel = new JPanel();
        fileOperationPanel.setLayout(new BoxLayout(fileOperationPanel, BoxLayout.X_AXIS));
        fileOperationPanel.setBorder(new TitledBorder("File Operation"));

        fileOperationPanel.add(createButton("Load", e -> loadFile()));
        fileOperationPanel.add(createButton("Store", e -> storeFile()));

        return fileOperationPanel;
    }

    private static void loadFile() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = jFileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            try {
                String filePath = file.getAbsolutePath();
                FileOperation.readFile(StorageListsOperation.getStorageLists(), filePath);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void storeFile() {
        JFileChooser jFileChooser = new JFileChooser();
        int option = jFileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            try {
                String filePath = file.getAbsolutePath();
                FileOperation.writeFile(StorageListsOperation.getStorageLists(), filePath);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static JPanel createTaskOperationPanel() {
        JPanel taskOperationPanel = new JPanel();
        taskOperationPanel.setLayout(new BoxLayout(taskOperationPanel, BoxLayout.X_AXIS));
        taskOperationPanel.setBorder(new TitledBorder("Task Operation"));

        JPanel simpleTaskOperationPanel = createSimpleTaskOperationPanel();
        taskOperationPanel.add(simpleTaskOperationPanel);

        JPanel compositeTaskOperationPanel = createCompositeTaskOperationPanel();
        taskOperationPanel.add(compositeTaskOperationPanel);

        taskOperationPanel.add(createButton("Delete Task", e -> deleteTask()));
        taskOperationPanel.add(createButton("Print Task", e -> printTask()));
        taskOperationPanel.add(createButton("Print All Task", e -> printAllTasks()));

        return taskOperationPanel;
    }

    private static void deleteTask() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Delete Task");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            String name = nameTextField.getText();
            try {
                String response = TaskOperation.deleteTask(StorageListsOperation.getStorageLists(), name);
                JOptionPane.showMessageDialog(dialog, response, "Task Deleted", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(deleteButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void printTask() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Print Task Details");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);

        JButton printButton = new JButton("Print");
        printButton.addActionListener(e -> {
            String name = nameTextField.getText();
            try {
                String taskDetails = TaskOperation.printTask(StorageListsOperation.getStorageLists(), name);
                JDialog detailsDialog = new JDialog();
                detailsDialog.setTitle("Task Details");
                JTextArea textArea = new JTextArea(taskDetails);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                detailsDialog.add(scrollPane);
                detailsDialog.pack();
                detailsDialog.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(printButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void printAllTasks() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Print All Tasks");

        JTextArea tasksTextArea = new JTextArea(ROWS1, COLUMNS2);
        tasksTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(tasksTextArea);

        try {
            String allTasks = TaskOperation.printAllTasks(StorageListsOperation.getStorageLists());
            tasksTextArea.setText(allTasks);
        } catch (Exception ex) {
            tasksTextArea.setText("Error occurred: " + ex.getMessage());
        }

        dialog.add(scrollPane);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static JPanel createSimpleTaskOperationPanel() {
        JPanel simpleTaskOperationPanel = new JPanel();
        simpleTaskOperationPanel.setLayout(new BoxLayout(simpleTaskOperationPanel, BoxLayout.Y_AXIS));
        simpleTaskOperationPanel.setBorder(new TitledBorder("Simple Task Operation"));

        simpleTaskOperationPanel.add(createButton("Create Simple Task", e -> createSimpleTask()));
        simpleTaskOperationPanel.add(createButton("Report Earliest Finish Time", e -> reportEarliestFinishTime()));

        JPanel changeSimpleTaskPanel = new JPanel();
        changeSimpleTaskPanel.setLayout(new BoxLayout(changeSimpleTaskPanel, BoxLayout.Y_AXIS));
        changeSimpleTaskPanel.setBorder(new TitledBorder("Change Simple Task"));

        changeSimpleTaskPanel.add(createButton("Change Name", e -> changeSimpleTaskName()));
        changeSimpleTaskPanel.add(createButton("Change Description", e -> changeSimpleTaskDescription()));
        changeSimpleTaskPanel.add(createButton("Change Duration", e -> changeSimpleTaskDuration()));
        changeSimpleTaskPanel.add(createButton("Change Prerequisites", e -> changeSimpleTaskPrerequisites()));

        simpleTaskOperationPanel.add(changeSimpleTaskPanel);

        return simpleTaskOperationPanel;
    }

    private static void createSimpleTask() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Simple Task");

        JLabel nameLabel = new JLabel("Name");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel descriptionLabel = new JLabel("Description");
        JTextField descriptionTextField = new JTextField(COLUMNS1);
        JLabel durationLabel = new JLabel("Duration");
        JTextField durationTextField = new JTextField(COLUMNS1);
        JLabel prerequisitesLabel = new JLabel("Prerequisites (comma-separated)");
        JTextField prerequisitesTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String description = descriptionTextField.getText();
            String durationStr = durationTextField.getText();
            String[] prerequisites = prerequisitesTextField.getText().split(",\\s*");

            try {
                double duration = Double.parseDouble(durationStr);
                TaskOperation.createSimpleTask(StorageListsOperation.getStorageLists(), name, description, durationStr, prerequisites);
                JOptionPane.showMessageDialog(dialog, "Primitive Task Created Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid duration format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(descriptionLabel);
        panel.add(descriptionTextField);
        panel.add(durationLabel);
        panel.add(durationTextField);
        panel.add(prerequisitesLabel);
        panel.add(prerequisitesTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void reportEarliestFinishTime() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Report Earliest Finish Time");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String taskName = nameTextField.getText();
            try {
                String finishTimeReport = TaskOperation.reportEarliestFinishTime(StorageListsOperation.getStorageLists(), taskName);
                JOptionPane.showMessageDialog(dialog, finishTimeReport, "Earliest Finish Time Report", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void changeSimpleTaskName() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Sample Task Name");

        JLabel currentNameLabel = new JLabel("Current Task Name:");
        JTextField currentNameTextField = new JTextField(COLUMNS1);
        JLabel newNameLabel = new JLabel("New Task Name:");
        JTextField newNameTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String currentName = currentNameTextField.getText();
            String newName = newNameTextField.getText();
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), currentName);
                CheckAvailability.checkName(newName);

                existingTask.setName(newName);
                JOptionPane.showMessageDialog(dialog, "Task name changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(currentNameLabel);
        panel.add(currentNameTextField);
        panel.add(newNameLabel);
        panel.add(newNameTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void changeSimpleTaskDescription() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Sample Task Description");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel newDescriptionLabel = new JLabel("New Description:");
        JTextField newDescriptionTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String newDescription = newDescriptionTextField.getText();
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), name);
                CheckAvailability.checkDescription(newDescription);

                if (existingTask instanceof PrimitiveTask) {
                    existingTask.setDescription(newDescription);
                    JOptionPane.showMessageDialog(dialog, "Task description changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Selected task is not a Primitive Task", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(newDescriptionLabel);
        panel.add(newDescriptionTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void changeSimpleTaskPrerequisites() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Simple Task Prerequisites");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel newPrerequisitesLabel = new JLabel("New Prerequisites (comma-separated):");
        JTextField newPrerequisitesTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String[] newPrerequisites = newPrerequisitesTextField.getText().split(",\\s*");
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), name);

                if (existingTask instanceof PrimitiveTask primitiveTask) {
                    primitiveTask.getList().clear(); // Clear existing prerequisites

                    for (String prerequisiteName : newPrerequisites) {
                        if (!prerequisiteName.trim().isEmpty()) {
                            Task prerequisiteTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), prerequisiteName.trim());
                            primitiveTask.addPrerequisites(prerequisiteTask);
                        }
                    }

                    JOptionPane.showMessageDialog(dialog, "Task prerequisites updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Selected task is not a Primitive Task", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(newPrerequisitesLabel);
        panel.add(newPrerequisitesTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }



    private static void changeSimpleTaskDuration() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Sample Task Duration");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel newDurationLabel = new JLabel("New Duration:");
        JTextField newDurationTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String newDurationStr = newDurationTextField.getText();
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), name);
                double newDuration = CheckAvailability.checkDuration(newDurationStr);

                if (existingTask instanceof PrimitiveTask) {
                    ((PrimitiveTask) existingTask).setDuration(newDuration);
                    JOptionPane.showMessageDialog(dialog, "Task duration changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Selected task is not a Primitive Task", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid duration format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(newDurationLabel);
        panel.add(newDurationTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }



    private static JPanel createCompositeTaskOperationPanel() {
        JPanel compositeTaskOperationPanel = new JPanel();
        compositeTaskOperationPanel.setLayout(new BoxLayout(compositeTaskOperationPanel, BoxLayout.Y_AXIS));
        compositeTaskOperationPanel.setBorder(new TitledBorder("Composite Task Operation"));

        compositeTaskOperationPanel.add(createButton("Create Composite Task", e -> createCompositeTask()));
        compositeTaskOperationPanel.add(createButton("Report Duration", e -> reportCompositeTaskDuration()));

        JPanel changeCompositeTaskPanel = new JPanel();
        changeCompositeTaskPanel.setLayout(new BoxLayout(changeCompositeTaskPanel, BoxLayout.Y_AXIS));
        changeCompositeTaskPanel.setBorder(new TitledBorder("Change Composite Task"));

        changeCompositeTaskPanel.add(createButton("Change Name", e -> changeCompositeTaskName()));
        changeCompositeTaskPanel.add(createButton("Change Description", e -> changeCompositeTaskDescription()));
        changeCompositeTaskPanel.add(createButton("Change Subtasks", e -> changeCompositeTaskSubtasks()));

        compositeTaskOperationPanel.add(changeCompositeTaskPanel);

        return compositeTaskOperationPanel;
    }

    private static void createCompositeTask() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create Composite Task");

        JLabel nameLabel = new JLabel("Name");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel descriptionLabel = new JLabel("Description");
        JTextField descriptionTextField = new JTextField(COLUMNS1);
        JLabel subtasksLabel = new JLabel("Subtasks");
        JTextField subtasksTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String description = descriptionTextField.getText();
            String[] subtasks = subtasksTextField.getText().split(",\\s*");

            try {
                TaskOperation.createCompositeTask(StorageListsOperation.getStorageLists(), name, description, subtasks);
                JOptionPane.showMessageDialog(dialog, "Composite Task Created Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(descriptionLabel);
        panel.add(descriptionTextField);
        panel.add(subtasksLabel);
        panel.add(subtasksTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void reportCompositeTaskDuration() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Report Composite Task Duration");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String taskName = nameTextField.getText();
            try {
                String durationReport = TaskOperation.reportDuration(StorageListsOperation.getStorageLists(), taskName);
                JOptionPane.showMessageDialog(dialog, durationReport, "Composite Task Duration Report", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void changeCompositeTaskName() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Composite Task Name");

        JLabel currentNameLabel = new JLabel("Current Task Name:");
        JTextField currentNameTextField = new JTextField(COLUMNS1);
        JLabel newNameLabel = new JLabel("New Task Name:");
        JTextField newNameTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String currentName = currentNameTextField.getText();
            String newName = newNameTextField.getText();
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), currentName);
                CheckAvailability.checkName(newName);

                if (existingTask instanceof CompositeTask) {
                    existingTask.setName(newName);
                    JOptionPane.showMessageDialog(dialog, "Composite Task name changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Selected task is not a Composite Task", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(currentNameLabel);
        panel.add(currentNameTextField);
        panel.add(newNameLabel);
        panel.add(newNameTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void changeCompositeTaskDescription() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Composite Task Description");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel newDescriptionLabel = new JLabel("New Description:");
        JTextField newDescriptionTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String newDescription = newDescriptionTextField.getText();
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), name);
                CheckAvailability.checkDescription(newDescription);

                if (existingTask instanceof CompositeTask) {
                    existingTask.setDescription(newDescription);
                    JOptionPane.showMessageDialog(dialog, "Composite Task description changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Selected task is not a Composite Task", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(newDescriptionLabel);
        panel.add(newDescriptionTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void changeCompositeTaskSubtasks() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Composite Task Subtasks");

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);
        JLabel newSubtasksLabel = new JLabel("New Subtasks (comma-separated):");
        JTextField newSubtasksTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String[] newSubtasks = newSubtasksTextField.getText().split(",\\s*");
            try {
                Task existingTask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), name);

                if (existingTask instanceof CompositeTask compositeTask) {
                    compositeTask.clearSubTasks(); // Clear existing subtasks

                    for (String subtaskName : newSubtasks) {
                        if (!subtaskName.trim().isEmpty()) {
                            Task subtask = CheckAvailability.checkTaskExists(StorageListsOperation.getStorageLists(), subtaskName.trim());
                            compositeTask.addTask(subtask);
                        }
                    }

                    JOptionPane.showMessageDialog(dialog, "Composite Task subtasks updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Selected task is not a Composite Task", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(newSubtasksLabel);
        panel.add(newSubtasksTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static JPanel createCriterionOperationPanel() {
        JPanel criterionOperationPanel = new JPanel();
        criterionOperationPanel.setLayout(new BoxLayout(criterionOperationPanel, BoxLayout.X_AXIS));
        criterionOperationPanel.setBorder(new TitledBorder("Criterion Operation"));

        JPanel defineCriterionPanel = createDefineCriterionOperationPanel();
        criterionOperationPanel.add(defineCriterionPanel);

        criterionOperationPanel.add(createButton("Delete Criterion", e -> deleteCriterion()));
        criterionOperationPanel.add(createButton("Print All Criteria", e -> printAllCriteria()));

        return criterionOperationPanel;
    }

    private static void deleteCriterion() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Delete Criterion");

        JLabel nameLabel = new JLabel("Criterion Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            String name = nameTextField.getText();
            try {
                String response = CriterionOperation.deleteCriteria(StorageListsOperation.getStorageLists(), name);
                JOptionPane.showMessageDialog(dialog, response, "Criterion Deleted", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(deleteButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void printAllCriteria() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Print All Criteria");

        JTextArea criteriaTextArea = new JTextArea(ROWS1, COLUMNS2);
        criteriaTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(criteriaTextArea);

        try {
            String allCriteria = CriterionOperation.printAllCriteria(StorageListsOperation.getStorageLists());
            criteriaTextArea.setText(allCriteria);
        } catch (Exception ex) {
            criteriaTextArea.setText("Error occurred: " + ex.getMessage());
        }

        dialog.add(scrollPane);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static JPanel createDefineCriterionOperationPanel() {
        JPanel defineCriterionOperationPanel = new JPanel();
        defineCriterionOperationPanel.setLayout(new BoxLayout(defineCriterionOperationPanel, BoxLayout.Y_AXIS));
        defineCriterionOperationPanel.setBorder(new TitledBorder("Define Criterion"));

        defineCriterionOperationPanel.add(createButton("Define Basic Criterion", e -> defineBasicCriterion()));
        defineCriterionOperationPanel.add(createButton("Define Negated Criterion", e -> defineNegatedCriterion()));
        defineCriterionOperationPanel.add(createButton("Define Binary Criterion", e -> defineBinaryCriterion()));

        return defineCriterionOperationPanel;
    }

    private static void defineBasicCriterion() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Define Basic Criterion");

        JTextField nameTextField = new JTextField(COLUMNS1);
        JTextField propertyTextField = new JTextField(COLUMNS1);
        JTextField operandTextField = new JTextField(COLUMNS1);
        JTextField valueTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String property = propertyTextField.getText();
            String operand = operandTextField.getText();
            String value = valueTextField.getText();
            try {
                String[] values = {value};
                CriterionOperation.defineBasicCriterion(
                        StorageListsOperation.getStorageLists(), name, property, operand, values);
                JOptionPane.showMessageDialog(dialog, "Basic Criterion defined successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameTextField);
        panel.add(new JLabel("Property:"));
        panel.add(propertyTextField);
        panel.add(new JLabel("Operand:"));
        panel.add(operandTextField);
        panel.add(new JLabel("Value:"));
        panel.add(valueTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void defineNegatedCriterion() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Define Negated Criterion");

        JTextField newCriterionNameTextField = new JTextField(COLUMNS1);
        JTextField existingCriterionNameTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String newCriterionName = newCriterionNameTextField.getText();
            String existingCriterionName = existingCriterionNameTextField.getText();
            try {
                CriterionOperation.defineNegatedCriterion(
                        StorageListsOperation.getStorageLists(), newCriterionName, existingCriterionName);
                JOptionPane.showMessageDialog(dialog, "Negated Criterion defined successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("New Criterion Name:"));
        panel.add(newCriterionNameTextField);
        panel.add(new JLabel("Existing Criterion Name:"));
        panel.add(existingCriterionNameTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static void defineBinaryCriterion() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Define Binary Criterion");

        JTextField newCriterionNameTextField = new JTextField(COLUMNS1);
        JTextField firstCriterionNameTextField = new JTextField(COLUMNS1);
        JTextField logicalOperatorTextField = new JTextField(COLUMNS1);
        JTextField secondCriterionNameTextField = new JTextField(COLUMNS1);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String newCriterionName = newCriterionNameTextField.getText();
            String firstCriterionName = firstCriterionNameTextField.getText();
            String logicalOperator = logicalOperatorTextField.getText();
            String secondCriterionName = secondCriterionNameTextField.getText();
            try {
                CriterionOperation.defineBinaryCriterion(
                        StorageListsOperation.getStorageLists(), newCriterionName, firstCriterionName, logicalOperator, secondCriterionName);
                JOptionPane.showMessageDialog(dialog, "Binary Criterion defined successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("New Criterion Name:"));
        panel.add(newCriterionNameTextField);
        panel.add(new JLabel("First Criterion Name:"));
        panel.add(firstCriterionNameTextField);
        panel.add(new JLabel("Logical Operator (&&, ||):"));
        panel.add(logicalOperatorTextField);
        panel.add(new JLabel("Second Criterion Name:"));
        panel.add(secondCriterionNameTextField);
        panel.add(submitButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static JPanel createSearchOperationPanel() {
        JPanel searchOperationPanel = new JPanel();
        searchOperationPanel.setLayout(new BoxLayout(searchOperationPanel, BoxLayout.Y_AXIS));
        searchOperationPanel.setBorder(new TitledBorder("Search Tasks"));

        searchOperationPanel.add(createButton("Search Tasks By Criterion", e -> searchTasksByCriterion()));

        return searchOperationPanel;
    }

    private static void searchTasksByCriterion() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Search Tasks By Criterion");

        JLabel nameLabel = new JLabel("Criterion Name:");
        JTextField nameTextField = new JTextField(COLUMNS1);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String criterionName = nameTextField.getText();
            try {
                String searchResults = CriterionOperation.search(StorageListsOperation.getStorageLists(), criterionName);

                JDialog resultDialog = new JDialog();
                resultDialog.setTitle("Search Results");
                resultDialog.setLayout(new BorderLayout());

                JTextArea resultTextArea = new JTextArea(ROWS1, COLUMNS2);
                resultTextArea.setText(searchResults);
                resultTextArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(resultTextArea);
                resultDialog.add(scrollPane, BorderLayout.CENTER);

                resultDialog.pack();
                resultDialog.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(searchButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    private static JPanel createStorageListsOperationPanel() {
        JPanel storageListsOperationPanel  = new JPanel();
        storageListsOperationPanel.setLayout(new BoxLayout(storageListsOperationPanel, BoxLayout.X_AXIS));
        storageListsOperationPanel.setBorder(new TitledBorder("Storage Lists Operation"));

        storageListsOperationPanel.add(createButton("Undo", e -> undo()));
        storageListsOperationPanel.add(createButton("Redo", e -> redo()));

        return storageListsOperationPanel;
    }

    private static void undo() {
        try {
            String result = StorageListsOperation.undo();
            JOptionPane.showMessageDialog(null, result, "Undo Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Undo Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void redo() {
        try {
            String result = StorageListsOperation.redo();
            JOptionPane.showMessageDialog(null, result, "Redo Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Redo Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JPanel createProgramOperationPanel() {
        JPanel programOperationPanel = new JPanel();
        programOperationPanel.setLayout(new BoxLayout(programOperationPanel, BoxLayout.Y_AXIS));
        programOperationPanel.setBorder(new TitledBorder("Program Operation"));

        programOperationPanel.add(createButton("Quit", e -> quit()));

        return programOperationPanel;
    }

    private static void quit() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}