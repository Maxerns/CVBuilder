/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author mxgar
 */
import java.awt.*;  

public class ReferencePanel extends JPanel {
    public ReferencePanel(String referenceText) {
        setLayout(new BorderLayout());

        // Split the referenceText into separate references based on double line breaks
        String[] references = referenceText.split("\n\n"); 

        // Create a grid layout panel to hold the references
        JPanel innerPanel = new JPanel(new GridLayout(references.length, 1)); 

        // Iterate over each reference
        for (String reference : references) {
            // Create a panel for each reference with a titled border
            JPanel sectionPanel = new JPanel(new BorderLayout());
            Border blackline = BorderFactory.createTitledBorder("Reference Section");
            sectionPanel.setBorder(blackline);

            // Split the reference into separate lines
            String[] lines = reference.trim().split("\n");

            // Create a text area to display the reference text
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            // Append each line of the reference to the text area
            for (String line : lines) {
                textArea.append(line.trim() + "\n");
            }

            // Add the text area to the section panel
            sectionPanel.add(textArea, BorderLayout.CENTER);

            // Add the section panel to the inner panel
            innerPanel.add(sectionPanel);
        }

        // Add the inner panel to the main panel
        add(innerPanel, BorderLayout.CENTER);
    }
}
    
    