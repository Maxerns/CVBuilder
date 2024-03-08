/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

/**
 *
 * @author mxgar
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;


public class ReferencePanel extends JPanel {
    public ReferencePanel(String referenceText) {
        setLayout(new BorderLayout());

        String[] references = referenceText.split("\n"); // Split into separate references
        JPanel innerPanel = new JPanel(new GridLayout(references.length, 1)); // Create a grid layout panel for references

        for (String reference : references) {
            JPanel sectionPanel = new JPanel(new BorderLayout());
            Border blackline = BorderFactory.createTitledBorder("Reference Section");
            sectionPanel.setBorder(blackline);

            JTextArea textArea = new JTextArea(reference.trim());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            sectionPanel.add(textArea, BorderLayout.CENTER);

            innerPanel.add(sectionPanel);
        }

        add(innerPanel, BorderLayout.CENTER);
    }
}