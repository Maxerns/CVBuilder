package cvbuilder.view;

import cvbuilder.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author mxgar
 */
public class UserPanel extends JPanel implements ActionListener {
    private JRadioButton dataRadioButton; // Radio button to display the data
    private JButton editButton; // Button to edit the data
    private JButton deleteButton; // Button to delete the data 
    private String dataType; // Type of data 
    private String data; // Data to be displayed
    private String references; // References for the data
    public static JCheckBox includeCheckBox; // Check box to include the data
    private static boolean isCheckBoxAdded = false; // Flag to check if the check box has been added

    public UserPanel() {
        // empty constructor
    }

    public UserPanel(String dataType, String data) {
        // Constructor that takes dataType and data as parameters
        this.dataType = dataType;
        this.data = data;

        // Set the layout of the panel to BorderLayout
        setLayout(new BorderLayout());

        // Create a JRadioButton with the data as the label
        dataRadioButton = new JRadioButton(data);
        dataRadioButton.setActionCommand(data);
        dataRadioButton.addActionListener(this);

        // Create Edit and Delete buttons
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        // Add action listeners to the buttons
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Set the preferred size of the buttons
        Dimension buttonSize = new Dimension(80, 15);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);

        // Create a panel to hold the components
        JPanel centerPanel = new JPanel();
        centerPanel.add(dataRadioButton);
        centerPanel.add(editButton);
        centerPanel.add(deleteButton);

        // If the dataType is "Title" and the includeCheckBox has not been added yet,
        // create a new JCheckBox and add it to the panel
        if (dataType.equals("Title") && !isCheckBoxAdded) {
            includeCheckBox = new JCheckBox("Include");
            JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            checkBoxPanel.add(includeCheckBox);
            add(checkBoxPanel, BorderLayout.NORTH);
            isCheckBoxAdded = true;
        }

        // Add the centerPanel to the UserPanel
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle the actions performed on the components
        if (e.getActionCommand().equals("Edit")) {
            // Show a dialog to edit the data
            String userInput = JOptionPane.showInputDialog("Enter New " + dataType, data);
            if (userInput != null && !userInput.isEmpty()) {
                data = userInput;
                dataRadioButton.setText(data);
            } else {
                JOptionPane.showMessageDialog(editButton, "Invalid input. Please enter a valid value.");
            }
        } else if (e.getActionCommand().equals("Delete")) {
            // Remove the UserPanel from its parent container
            Container parent = this.getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        } else if (e.getActionCommand().equals("Add Profile")) {
            // Show a dialog to add a new user profile
            JTextField titleField = new JTextField(10);
            JTextField nameField = new JTextField(10);
            JTextField emailField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Title:"));
            panel.add(titleField);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Add New User Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Get the user input and create a new User object
                String title = titleField.getText();
                String name = nameField.getText();
                String email = emailField.getText();

                // Validate the input fields
                if (title.isEmpty() || name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.contains("@") || email.indexOf('@') > email.lastIndexOf('.')) {
                    JOptionPane.showMessageDialog(this, "Invalid email address. Please include '@' and a domain in the email.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newUser = new User(title, name, email, references);
                appendToCSVFile(newUser);
            }
        } else if (e.getActionCommand().equals("Next Section")) {
            // Move to the next section in a JTabbedPane
            JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
            int currentIndex = tabbedPane.getSelectedIndex();
            int nextIndex = currentIndex + 1;
            if (nextIndex < tabbedPane.getTabCount()) {
                tabbedPane.setSelectedIndex(nextIndex);
            }
        } else if (e.getActionCommand().equals("Previous Section")) {
            // Move to the previous section in a JTabbedPane
            JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
            int currentIndex = tabbedPane.getSelectedIndex();
            int previousIndex = currentIndex - 1;
            if (previousIndex >= 0) {
                tabbedPane.setSelectedIndex(previousIndex);
            }
        }
    }

    // This method appends a user's information to a CSV file.
    public static void appendToCSVFile(User user) {
        try {
            // Create a FileWriter and BufferedWriter to write to the file.
            FileWriter fileWriter = new FileWriter("data\\cv_repo_3.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    
            // Write the user's title, name, and email to the file in the required format.
            bufferedWriter.write("User,Title," + user.getTitle());
            bufferedWriter.newLine();
            bufferedWriter.write("User,Name," + user.getName());
            bufferedWriter.newLine();
            bufferedWriter.write("User,Email," + user.getEmail());
            bufferedWriter.newLine();
    
            // Close the BufferedWriter and FileWriter.
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            // If an IOException occurs, print the stack trace.
            e.printStackTrace();
        }
    }

    // Getter methods for various fields
    public JRadioButton getDataRadioButton() {
        return dataRadioButton;
    }

    public String getDataType() {
        return dataType;
    }

    public String getData() {
        return data;
    }

    public JCheckBox getIncludeCheckBox() {
        return includeCheckBox;
    }
}