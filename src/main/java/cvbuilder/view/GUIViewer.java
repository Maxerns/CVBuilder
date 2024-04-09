/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import cvbuilder.model.User;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author mxgar
 */
public class GUIViewer extends JFrame implements ActionListener {

    private JTabbedPane mainTabbedPane;
    private static GUIViewer instance;

    private GUIViewer() {
        // Empty constructor
    }

    public static GUIViewer getInstance() {
        if (instance == null) {
            instance = new GUIViewer();
        }
        return instance;
    }

    public GUIViewer(List<User> users) {
        JMenuBar jmb = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(openItem);
        fileMenu.add(quitItem);
        jmb.add(fileMenu);
        setJMenuBar(jmb);
        quitItem.addActionListener(this);
        openItem.addActionListener(this);

        setTitle("User Profiler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainTabbedPane = new JTabbedPane();

        // Create a tabbed pane for User
        JTabbedPane userTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("User", userTabbedPane);

        JPanel namePanel = new JPanel(new GridLayout(0, 1)); // GridLayout with dynamic rows
        userTabbedPane.addTab("Name", namePanel);

        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        userTabbedPane.addTab("Title", titlePanel);

        JPanel emailPanel = new JPanel(new GridLayout(0, 1));
        userTabbedPane.addTab("Email", emailPanel);

        // Create a tabbed pane for References
        JTabbedPane referencesTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("References", referencesTabbedPane);

        // Add user profiles and references to the respective panels
        for (User user : users) {
            // Add user details to the respective panels
            String[] names = user.getName().split(",");
            for (String name : names) {
                namePanel.add(new UserPanel(0, "Name", name.trim()));
            }

            String[] titles = user.getTitle().split(",");
            for (String title : titles) {
                titlePanel.add(new UserPanel(0, "Title", title.trim()));
            }

            String[] emails = user.getEmail().split(",");
          for (String email : emails) {
    emailPanel.add(new UserPanel(0, "Email", email.trim()));
}

// Add references to the respective panels
String[] references = user.getReferences().trim().split(",");
for (int i = 0; i < references.length; i++) {
    String reference = references[i];
    ReferencePanel refPanel = new ReferencePanel(reference.trim());

    // Add each reference to a separate tab
    if (i == 0) {
        referencesTabbedPane.addTab("Referee 1", refPanel);
    } else if (i == 1) {
        referencesTabbedPane.addTab("Referee 1", refPanel);
    } else if (i == 2) {
        referencesTabbedPane.addTab("Referee 2", refPanel);
    }
}

add(mainTabbedPane, BorderLayout.CENTER); }
}

public void showViewer() {
    setVisible(true);
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Quit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Open")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        }
    }

    public List<User> readUserProfilesFromFile() {
        String filePath = "data\\cv_repo_3.csv";
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String title = "";
            String name = "";
            String email = "";
            String references = "";
            boolean isUserSection = false;
            boolean isReferenceSection = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equalsIgnoreCase("Section") && fields[1].equalsIgnoreCase("Sub-Section")) {
                    // Skip the header line
                    continue;
                }

                if (fields[0].equalsIgnoreCase("User")) {
                    isUserSection = true;
                    isReferenceSection = false;
                } else if (fields[0].equalsIgnoreCase("References")) {
                    isUserSection = false;
                    isReferenceSection = true;
                } else {
                    isUserSection = false;
                    isReferenceSection = false;
                }

                if (isUserSection) {
                    if (fields[1].equalsIgnoreCase("Name")) {
                        name = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                    } else if (fields[1].equalsIgnoreCase("Title")) {
                        title = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                    } else if (fields[1].equalsIgnoreCase("Email")) {
                        email = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                        User user = new User(title, name, email, references);
                        users.add(user);
                        title = "";
                        name = "";
                        email = "";
                    }
                } else if (isReferenceSection) {
                    if (fields[1].equalsIgnoreCase("Referee 1") || fields[1].equalsIgnoreCase("Referee 2")) {
                        String referenceText = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                        referenceText = referenceText.replaceAll("%%%%", " ");
                        referenceText = referenceText.replaceAll("////", ",");
                        references += referenceText + "\n";
                    }

                    if (isReferenceSection && fields[1].equalsIgnoreCase("Referee 2")) {
                        User user = new User(title, name, email, references);
                        users.add(user);
                        title = "";
                        name = "";
                        email = "";
                        references = "";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}