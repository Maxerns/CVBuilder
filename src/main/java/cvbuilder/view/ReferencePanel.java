/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
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
    // Lists to hold the JTextAreas and JButtons
    private List<JTextArea> referenceTextAreas;
    private List<JButton> addButtons;

    public ReferencePanel(String referenceText) {
        // Set the layout of the panel
        setLayout(new BorderLayout());

        // Split the referenceText into separate references based on double line breaks
        String[] references = referenceText.split("\n\n");

        // Create a grid layout panel to hold the references
        JPanel innerPanel = new JPanel(new GridLayout(references.length, 1, 10, 10));
        innerPanel.setPreferredSize(new Dimension(500, 400));
        innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the inner panel

        // Initialize the lists
        referenceTextAreas = new ArrayList<>();
        addButtons = new ArrayList<>();

        // Iterate over each reference
        for (String reference : references) {
            // Create a panel for each reference with a titled border
            JPanel sectionPanel = new JPanel(new BorderLayout());
            Border blackline = BorderFactory.createTitledBorder("Reference Section");
            sectionPanel.setBorder(BorderFactory.createCompoundBorder(blackline, BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Add padding around the section panel
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

            // Add the text area to the section panel
            sectionPanel.add(sectionTextArea, BorderLayout.CENTER);

            // Create a new text area and add button for the reference
            JTextArea referenceTextArea = new JTextArea();
            referenceTextArea.setPreferredSize(new Dimension(150, 40));
            referenceTextAreas.add(referenceTextArea);

            // Determine the next referee number
            int nextRefNumber = getNextRefereeNumber();

            // Create an "Add Reference" button and add an action listener to it
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

            // Create a panel to hold the text area and add button
            JPanel referencePanel = new JPanel(new BorderLayout());
            referencePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding around the reference panel
            referencePanel.add(referenceTextArea, BorderLayout.CENTER);
            referencePanel.add(addButton, BorderLayout.EAST);

            // Add the reference panel to the section panel
            sectionPanel.add(referencePanel, BorderLayout.SOUTH);

            // Add the section panel to the inner panel
            innerPanel.add(sectionPanel);
        }

        // Add the inner panel to the main panel
        add(innerPanel, BorderLayout.CENTER);

        // Create a panel to hold the text areas and add buttons
        JPanel bottomPanel = new JPanel(new GridLayout(referenceTextAreas.size(), 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the bottom panel
        for (int i = 0; i < referenceTextAreas.size(); i++) {
            bottomPanel.add(referenceTextAreas.get(i));
            bottomPanel.add(addButtons.get(i));
        }
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to get the next referee number
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

    // Method to append the reference to the CSV file
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
}