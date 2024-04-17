/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mxgar
 */

public class ReferencePanel extends JPanel {
    private List<JTextArea> referenceTextAreas;
    private List<JButton> addButtons;
    private List<JButton> editButtons;
    private List<JButton> deleteButtons;
    private List<JRadioButton> radioButtons;
    private ButtonGroup radioButtonGroup;

    public ReferencePanel(String referenceText) {
        setLayout(new BorderLayout());

        // Split the referenceText into separate references based on double line breaks
        String[] references = referenceText.split("\n\n");

        // Create a grid layout panel to hold the references
        JPanel innerPanel = new JPanel(new GridLayout(references.length, 1, 10, 10));
        innerPanel.setPreferredSize(new Dimension(500, 400));
        innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the inner panel

        referenceTextAreas = new ArrayList<>();
        addButtons = new ArrayList<>();
        editButtons = new ArrayList<>();
        deleteButtons = new ArrayList<>();
        radioButtons = new ArrayList<>();
        radioButtonGroup = new ButtonGroup();

        // Iterate over each reference
        for (String reference : references) {
            // Create a panel for each reference with a titled border
            JPanel sectionPanel = new JPanel(new BorderLayout());
            Border blackline = BorderFactory.createTitledBorder("Reference Section");
            sectionPanel.setBorder(
                    BorderFactory.createCompoundBorder(blackline, BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Add
                                                                                                                     // padding
                                                                                                                     // around
                                                                                                                     // the
                                                                                                                     // section
                                                                                                                     // panel
            sectionPanel.setPreferredSize(new Dimension(450, 100));

            // Split the reference into separate lines
            String[] lines = reference.trim().split("\n");

            // Create a text area to display the reference text
            JTextArea sectionTextArea = new JTextArea();
            sectionTextArea.setEditable(false);
            sectionTextArea.setLineWrap(true);
            sectionTextArea.setWrapStyleWord(true);
            sectionTextArea.setPreferredSize(new Dimension(400, 80));

            // Append each line of the reference to the text area
            for (String line : lines) {
                sectionTextArea.append(line.trim() + "\n");
            }

            // Create a radio button for the reference
            JRadioButton radioButton = new JRadioButton();
            radioButtons.add(radioButton);
            radioButtonGroup.add(radioButton);

            // Create a panel to hold the radio button and text area
            JPanel textAndRadioButtonPanel = new JPanel(new BorderLayout());
            textAndRadioButtonPanel.add(radioButton, BorderLayout.WEST);
            textAndRadioButtonPanel.add(sectionTextArea, BorderLayout.CENTER);

            // Add the panel to the section panel instead of the text area
            sectionPanel.add(textAndRadioButtonPanel, BorderLayout.CENTER);

            // Create a checkbox for the reference
            JCheckBox includeCheckBox = new JCheckBox("Include");

            // Add the checkbox to the section panel
            sectionPanel.add(includeCheckBox, BorderLayout.NORTH);

            // Create a new text area, add button, edit button, and delete button for the
            // reference
            JTextArea referenceTextArea = new JTextArea();
            referenceTextArea.setPreferredSize(new Dimension(150, 40));
            referenceTextAreas.add(referenceTextArea);

            // Determine the next referee number
            int nextRefNumber = getNextRefereeNumber();

            JButton addButton = new JButton("Add Reference");
            addButton.addActionListener(e -> {
                String referenceText1 = referenceTextArea.getText();
                if (!referenceText1.isEmpty()) {
                    // Append the reference to the CSV file
                    appendToCSVFile(referenceText1, nextRefNumber);
                    // Clear the text area
                    referenceTextArea.setText("");
                }
            });
            addButtons.add(addButton);

            JButton editButton = new JButton("Edit");
            editButton.addActionListener(e -> {
                String currentReference = sectionTextArea.getText().replace("\n", "////");
                String editedReference = JOptionPane.showInputDialog("Edit Reference", currentReference);
                if (editedReference != null && !editedReference.isEmpty()) {
                    updateReferenceInCSVFile(nextRefNumber, editedReference);
                    sectionTextArea.setText(editedReference.replaceAll("////", "\n"));
                }
            });
            editButtons.add(editButton);

            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> {
                deleteReferenceFromCSVFile(nextRefNumber);
                sectionPanel.setVisible(false);
            });
            deleteButtons.add(deleteButton);

            // Create a panel to hold the text area, add button, edit button, and delete
            // button
            JPanel referencePanel = new JPanel(new BorderLayout());
            referencePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding around the reference
                                                                                   // panel
            referencePanel.add(referenceTextArea, BorderLayout.CENTER);
            referencePanel.add(addButton, BorderLayout.EAST);
            referencePanel.add(editButton, BorderLayout.WEST);
            referencePanel.add(deleteButton, BorderLayout.SOUTH);

            // Add the reference panel to the section panel
            sectionPanel.add(referencePanel, BorderLayout.SOUTH);

            // Add the section panel to the inner panel
            innerPanel.add(sectionPanel);
        }

        // Add the inner panel to the main panel
        add(innerPanel, BorderLayout.CENTER);

        // Create a panel to hold the text areas, add buttons, edit buttons, and delete
        // buttons
        JPanel bottomPanel = new JPanel(new GridLayout(referenceTextAreas.size(), 4, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the bottom panel
        for (int i = 0; i < referenceTextAreas.size(); i++) {
            bottomPanel.add(referenceTextAreas.get(i));
            bottomPanel.add(addButtons.get(i));
            bottomPanel.add(editButtons.get(i));
            bottomPanel.add(deleteButtons.get(i));
        }
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private int getNextRefereeNumber() {
        try {
            // Read the CSV file and find the highest referee number
            List<String> lines = Files.readAllLines(Paths.get("data\\cv_repo_3.csv"));
            int maxRefNumber = 0;
            for (String line : lines) {
                if (line.startsWith("References,Referee")) {
                    int refNumber = Integer.parseInt(line.split(",")[1].replace("Referee ", ""));
                    if (refNumber > maxRefNumber) {
                        maxRefNumber = refNumber;
                    }
                }
            }
            return maxRefNumber + 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private void appendToCSVFile(String referenceText, int refNumber) {
        try {
            FileWriter fileWriter = new FileWriter("data\\cv_repo_3.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the reference text to the CSV file
            bufferedWriter.write("References,Referee " + refNumber + "," + referenceText.replace("\n", "////"));
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateReferenceInCSVFile(int refNumber, String newReferenceText) {
        try {
            // Read the CSV file into a list of lines
            List<String> lines = Files.readAllLines(Paths.get("data\\cv_repo_3.csv"));

            // Find the line with the reference to update
            int lineIndex = -1;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("References,Referee " + refNumber)) {
                    lineIndex = i;
                    break;
                }
            }

            // Update the reference text in the line
            if (lineIndex != -1) {
                lines.set(lineIndex, "References,Referee " + refNumber + "," + newReferenceText.replace("\n", "////"));
            }

            // Write the updated lines back to the CSV file
            FileWriter fileWriter = new FileWriter("data\\cv_repo_3.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteReferenceFromCSVFile(int refNumber) {
        try {
            // Read the CSV file into a list of lines
            List<String> lines = Files.readAllLines(Paths.get("data\\cv_repo_3.csv"));

            // Remove the line with the reference to delete
            int lineIndex = -1;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("References,Referee " + refNumber)) {
                    lineIndex = i;
                    break;
                }
            }
            if (lineIndex != -1) {
                lines.remove(lineIndex);
            }

            // Write the updated lines back to the CSV file
            FileWriter fileWriter = new FileWriter("data\\cv_repo_3.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}