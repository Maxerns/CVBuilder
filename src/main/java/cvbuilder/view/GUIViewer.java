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
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;


/**
 *
 * @author mxgar
 */
public class GUIViewer extends JFrame implements ActionListener {

    private JTabbedPane mainTabbedPane; // The main tabbed pane for the GUIViewer
    private static GUIViewer instance; // The instance of GUIViewer
    private List<File> filesToRead; // List of files to read
    private File lastAddedFile; // The last added file
    private Preferences prefs; // Preferences object to store the path of the last added file
    private JButton prevSectionButton; // Button to navigate to the previous section
    private JButton nextSectionButton; // Button to navigate to the next section
    private JButton addProfileButton; // Button to add a new profile
    private String prevSectionCommand; // Command for the previous section button
    private String nextSectionCommand; // Command for the next section button
    private String addProfileCommand; // Command for the add profile button   

    private GUIViewer() {
        filesToRead = new ArrayList<>();
        filesToRead.add(new File("data\\cv_repo_3.csv")); // The default file that is read
        initPrefs(); // Initialize preferences
    }

    private void initPrefs() {
        if (prefs == null) {
            prefs = Preferences.userNodeForPackage(GUIViewer.class); // Get the preferences node for the GUIViewer class
            String lastAddedFileName = prefs.get("lastAddedFile", null); // Get the path of the last added file from preferences
            if (lastAddedFileName != null) {
                lastAddedFile = new File(lastAddedFileName); // Create a File object for the last added file
                filesToRead.add(lastAddedFile); // Add the last added file to the list of files to read
            }
        }
    }

    public static GUIViewer getInstance() {
        if (instance == null) {
            instance = new GUIViewer(); // Create a new instance of GUIViewer if it doesn't exist
        }
        return instance; // Return the instance of GUIViewer
    }

    public GUIViewer(List<User> users) {
        filesToRead = new ArrayList<>();
        filesToRead.add(new File("data\\cv_repo_3.csv")); // Add a default file to read

        // Create the menu bar
        JMenuBar jmb = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(loadItem);
        fileMenu.add(quitItem);
        jmb.add(fileMenu);
        setJMenuBar(jmb);
        quitItem.addActionListener(this);
        loadItem.addActionListener(this);

        setTitle("Cv Builder");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainTabbedPane = new JTabbedPane();

         // Initialize the navigation buttons and action commands
         prevSectionButton = new JButton("Previous Section");
         nextSectionButton = new JButton("Next Section");
         addProfileButton = new JButton("Add Profile");
         prevSectionCommand = "Previous Section";
         nextSectionCommand = "Next Section";
         addProfileCommand = "Add Profile";

        // Create a tabbed pane for User
        JTabbedPane userTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("User", userTabbedPane);

        // Create a panel with a grid layout for names
        JPanel namePanel = new JPanel(new GridLayout(0, 1));
        UserPanel userNamePanel = new UserPanel();
        userNamePanel.addNavigationButtons(prevSectionButton, nextSectionButton, addProfileButton, prevSectionCommand, nextSectionCommand, addProfileCommand);
        namePanel.add(userNamePanel);
        userTabbedPane.addTab("Name", namePanel);

        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        UserPanel userTitlePanel = new UserPanel();
        userTitlePanel.addNavigationButtons(prevSectionButton, nextSectionButton, addProfileButton, prevSectionCommand, nextSectionCommand, addProfileCommand);
        titlePanel.add(userTitlePanel);
        userTabbedPane.addTab("Title", titlePanel);

        // Create the title checkbox
        JCheckBox includeTitleCheckBox = new JCheckBox("Include");
        titlePanel.add(includeTitleCheckBox);

        JPanel emailPanel = new JPanel(new GridLayout(0, 1));
        UserPanel userEmailPanel = new UserPanel();
        userEmailPanel.addNavigationButtons(prevSectionButton, nextSectionButton, addProfileButton, prevSectionCommand, nextSectionCommand, addProfileCommand);
        emailPanel.add(userEmailPanel);
        userTabbedPane.addTab("Email", emailPanel);

        // Create a tabbed pane for References
        JTabbedPane referencesTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("References", referencesTabbedPane);

        // Add user profiles and references to the respective panels
        for (User user : users) {
            // Add user details to the respective panels
            String[] names = user.getName().split(",");
            for (String name : names) {
                UserPanel nameUserPanel = new UserPanel("Name", name.trim());
                nameUserPanel.addNavigationButtons(prevSectionButton, nextSectionButton, addProfileButton, prevSectionCommand, nextSectionCommand, addProfileCommand);
                namePanel.add(nameUserPanel);
            }

            String[] titles = user.getTitle().split(",");
            for (String title : titles) {
                UserPanel titleUserPanel = new UserPanel("Title", title.trim());
                titleUserPanel.addNavigationButtons(prevSectionButton, nextSectionButton, addProfileButton, prevSectionCommand, nextSectionCommand, addProfileCommand);
                titlePanel.add(titleUserPanel);
            }

            String[] emails = user.getEmail().split(",");
            for (String email : emails) {
                UserPanel emailUserPanel = new UserPanel("Email", email.trim());
                emailUserPanel.addNavigationButtons(prevSectionButton, nextSectionButton, addProfileButton, prevSectionCommand, nextSectionCommand, addProfileCommand);
                emailPanel.add(emailUserPanel);
            }

            // Add references to the respective panels
            String[] references = user.getReferences().trim().split(",");
            for (int i = 0; i < references.length; i++) {
                String reference = references[i];
                ReferencePanel refPanel = createReferencePanel(reference);
                // Add each reference to a separate tab
                if (i == 0) {
                    referencesTabbedPane.addTab("Referee 1", refPanel);
                } else if (i == 1) {
                    referencesTabbedPane.addTab("Referee 2", refPanel);
                } else if (i == 2) {
                    referencesTabbedPane.addTab("Referee 3", refPanel);
                }
            }
        }
        add(mainTabbedPane, BorderLayout.CENTER); // Add the main tabbed pane to the center of the frame

        prevSectionButton.addActionListener(this);
        nextSectionButton.addActionListener(this);
        addProfileButton.addActionListener(this);
    }

    public void showViewer() {
        setVisible(true); // Set the GUIViewer frame visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initPrefs(); // Initialize the preferences

        if (e.getActionCommand().equals("Quit")) {
            System.exit(0); // Exit the program if "Quit" is selected
        } else if (e.getActionCommand().equals("Load")) {
            // Open a file dialog to choose a file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                // If a file is selected, copy it to the "data" directory and add it to the list of files to read
                File selectedFile = fileChooser.getSelectedFile();
                File dataDir = new File("data");
                if (!dataDir.exists()) {
                    dataDir.mkdir();
                }

                File newFile = new File(dataDir, selectedFile.getName());
                try {
                    Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    filesToRead.add(newFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                filesToRead.add(newFile);
                lastAddedFile = newFile;
                prefs.put("lastAddedFile", newFile.getPath()); // Save the path of the last added file in preferences
            }
        }
    }

    public List<User> readUserProfilesFromFile(File file) {
        // Read user profiles from a given file and return a list of User objects
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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

    public List<User> readUserProfilesFromAllFiles() {
        List<User> users = new ArrayList<>();
        for (File file : filesToRead) {
            users.addAll(readUserProfilesFromFile(file));
        }
        return users;
    }

    private ReferencePanel createReferencePanel(String reference) {
        ReferencePanel refPanel = new ReferencePanel(reference.trim());
        refPanel.addNavigationButtons();
        return refPanel;
    }
}