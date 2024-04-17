/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import cvbuilder.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.border.Border;


/**
 *
 * @author mxgar
 */
public class UserPanel extends JPanel implements ActionListener {
    private JRadioButton dataRadioButton;
    private JButton editButton;
    private JButton deleteButton;
    private String dataType;
    private String data;
    private String references;
    private static JCheckBox includeCheckBox;
    private static boolean isCheckBoxAdded = false;

    public UserPanel() {
        // empty constructor
    }

    public UserPanel(String dataType, String data) {
        this.dataType = dataType;
        this.data = data;

        setLayout(new BorderLayout());

        dataRadioButton = new JRadioButton(data);
        dataRadioButton.setActionCommand(data);
        dataRadioButton.addActionListener(this);

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        editButton.addActionListener(this);
        deleteButton.addActionListener(this);

        Dimension buttonSize = new Dimension(80, 15);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);

        JPanel centerPanel = new JPanel();
        centerPanel.add(dataRadioButton);
        centerPanel.add(editButton);
        centerPanel.add(deleteButton);
        if (dataType.equals("Title") && !isCheckBoxAdded) {
            includeCheckBox = new JCheckBox("Include");
            JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            checkBoxPanel.add(includeCheckBox);
            add(checkBoxPanel, BorderLayout.NORTH);
            isCheckBoxAdded = true;
        }

        
        add(centerPanel, BorderLayout.CENTER);

    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Edit")) {
            String userInput = JOptionPane.showInputDialog("Enter New " + dataType, data);
            if (userInput != null && !userInput.isEmpty()) {
                data = userInput;
                dataRadioButton.setText(data);
            } else {
                JOptionPane.showMessageDialog(editButton, "Invalid input. Please enter a valid value.");
            }
        } else if (e.getActionCommand().equals("Delete")) {
            Container parent = this.getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        } else if (e.getActionCommand().equals("Add Profile")) {
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
                String title = titleField.getText();
                String name = nameField.getText();
                String email = emailField.getText();

                // Making it so you have to have input for all the required fields
                if (title.isEmpty() || name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validating if the email entered is a valid input
                if (!email.contains("@") || email.indexOf('@') > email.lastIndexOf('.')) {
                    JOptionPane.showMessageDialog(this, "Invalid email address. Please include '@' and a domain in the email.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newUser = new User(title, name, email, references);
                appendToCSVFile(newUser);
            }
        } else if (e.getActionCommand().equals("Next Section")) {
            // Move to the next section
            // Get the ancestor of the current component that is of type JTabbedPane
            JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
            // Get the index of the currently selected tab
            int currentIndex = tabbedPane.getSelectedIndex();
            // Calculate the index of the next tab
            int nextIndex = currentIndex + 1;
            // Check if the next index is within the range of available tabs
            if (nextIndex < tabbedPane.getTabCount()) {
                // Set the next tab as the selected tab
               tabbedPane.setSelectedIndex(nextIndex); 
            }
        } else if (e.getActionCommand().equals("Previous Section")) {
            // Move to the previous section
            // Get the ancestor of the current component that is of type JTabbedPane
            JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
            // Get the index of the currently selected tab
            int currentIndex = tabbedPane.getSelectedIndex();
            // Calculate the index of the previous tab
            int previousIndex = currentIndex - 1;
            // Check if the previous index is within the range of available tabs
            if (previousIndex >= 0) {
                // Set the previous tab as the selected tab
                tabbedPane.setSelectedIndex(previousIndex);
            }
        } 
    }

    public static void appendToCSVFile(User user) {
        try {
            FileWriter fileWriter = new FileWriter("data\\cv_repo_3.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    
            // Write user data in the required format
            bufferedWriter.write("User,Title," + user.getTitle());
            bufferedWriter.newLine();
            bufferedWriter.write("User,Name," + user.getName());
            bufferedWriter.newLine();
            bufferedWriter.write("User,Email," + user.getEmail());
            bufferedWriter.newLine();
    
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




