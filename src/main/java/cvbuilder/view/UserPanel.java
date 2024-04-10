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
    private JButton addProfileButton;
    private String dataType;
    private String data;
    private int userCount;
    private User user;
    private String references;

    public UserPanel(int userCount, String dataType, String data) {
        this.userCount = userCount;
        this.dataType = dataType;
        this.data = data;
        setLayout(new BorderLayout());

        dataRadioButton = new JRadioButton(data);
        dataRadioButton.setActionCommand(data);
        dataRadioButton.addActionListener(this);

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        addProfileButton = new JButton("Add Profile");

        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        addProfileButton.addActionListener(this);

        Dimension buttonSize = new Dimension(80, 15);
        Dimension buttonSize1 = new Dimension(120, 15);

        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        addProfileButton.setPreferredSize(buttonSize1);

        JPanel centerPanel = new JPanel();
        centerPanel.add(dataRadioButton);
        centerPanel.add(editButton);
        centerPanel.add(deleteButton);
        add(centerPanel, BorderLayout.CENTER);

        JPanel outsidePanel = new JPanel();
        outsidePanel.add(addProfileButton);
        add(outsidePanel, BorderLayout.SOUTH);

        // Border blackline = BorderFactory.createTitledBorder(dataType);
        // setBorder(blackline);
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
            JCheckBox includeTitleCheckBox = new JCheckBox("Include");
            JTextField nameField = new JTextField(10);
            JTextField emailField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Title:"));
            panel.add(includeTitleCheckBox);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Add New User Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                String name = nameField.getText();
                String email = emailField.getText();

                User newUser = new User(title, name, email, references);
                appendToCSVFile(newUser);
            }
        }
    }

    private void appendToCSVFile(User user) {
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




