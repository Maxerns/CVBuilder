/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import cvbuilder.model.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;


/**
 *
 * @author mxgar
 */
public class GUIViewer extends JFrame implements ActionListener {
    private JTabbedPane mainTabbedPane;
    private static GUIViewer instance;
    private List<File> filesToRead;
    private File lastAddedFile;
    private Preferences prefs;
    private String references;
    

    private GUIViewer() {
        filesToRead = new ArrayList<>();
        filesToRead.add(new File("data\\cv_repo_3.csv"));
        initPrefs();
    }

    private void initPrefs() {
        if (prefs == null) {
            prefs = Preferences.userNodeForPackage(GUIViewer.class);
            String lastAddedFileName = prefs.get("lastAddedFile", null);
            if (lastAddedFileName != null) {
                lastAddedFile = new File(lastAddedFileName);
                filesToRead.add(lastAddedFile);
            }
        }
    }

    public static GUIViewer getInstance() {
        if (instance == null) {
            instance = new GUIViewer();
        }
        return instance;
    }

    public GUIViewer(List<User> users) {
        filesToRead = new ArrayList<>();
        filesToRead.add(new File("data\\cv_repo_3.csv"));

        // Create the menu bar and set up the menu items
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

        // Create the user-related panels and add them to the user tabbed pane
        JTabbedPane userTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("User", userTabbedPane);

        // Create the name, title, and email panels and add them to the user tabbed pane
        JPanel namePanel = new JPanel(new GridLayout(0, 1));
        UserPanel userNamePanel = new UserPanel("Name", "");
        namePanel.add(userNamePanel);
        userTabbedPane.addTab("Name", namePanel);

        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        UserPanel userTitlePanel = new UserPanel("Title", "");
        titlePanel.add(userTitlePanel);
        userTabbedPane.addTab("Title", titlePanel);

        JPanel emailPanel = new JPanel(new GridLayout(0, 1));
        UserPanel userEmailPanel = new UserPanel("Email", "");
        emailPanel.add(userEmailPanel);
        userTabbedPane.addTab("Email", emailPanel);

        // Create the references tabbed pane and add it to the main tabbed pane
        JTabbedPane referencesTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("References", referencesTabbedPane);

        // Add user profiles and references to the respective panels
        for (User user : users) {
            addUserDetailsToPanel(user, namePanel, titlePanel, emailPanel);
            addReferencesToPanel(user, referencesTabbedPane);
        }

        add(mainTabbedPane, BorderLayout.CENTER);

        // Add the navigation buttons to the bottom of the main tabbed pane
        addNavigationButtons();
    }

    private void addUserDetailsToPanel(User user, JPanel namePanel, JPanel titlePanel, JPanel emailPanel) {
        // Add name details
        String[] names = user.getName().split(",");
        for (String name : names) {
            UserPanel nameUserPanel = new UserPanel("Name", name.trim());
            namePanel.add(nameUserPanel);
        }

        // Add title details
        String[] titles = user.getTitle().split(",");
        for (String title : titles) {
            UserPanel titleUserPanel = new UserPanel("Title", title.trim());
            titlePanel.add(titleUserPanel);
        }

        // Add email details
        String[] emails = user.getEmail().split(",");
        for (String email : emails) {
            UserPanel emailUserPanel = new UserPanel("Email", email.trim());
            emailPanel.add(emailUserPanel);
        }
    }

    private void addReferencesToPanel(User user, JTabbedPane referencesTabbedPane) {
        String[] references = user.getReferences().trim().split(",");
        for (int i = 0; i < references.length; i++) {
            String reference = references[i];
            ReferencePanel refPanel = new ReferencePanel(reference);
            if (i == 0) {
                referencesTabbedPane.addTab("Referee 1", refPanel);
            } else if (i == 1) {
                referencesTabbedPane.addTab("Referee 2", refPanel);
            } else if (i == 2) {
                referencesTabbedPane.addTab("Referee 3", refPanel);
            }
        }
    }

  

    public void showViewer() {
        setVisible(true);
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


    @Override
    public void actionPerformed(ActionEvent e) {
        initPrefs();

        if (e.getActionCommand().equals("Quit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Load")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
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
                prefs.put("lastAddedFile", newFile.getPath());
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
        }  else if (e.getActionCommand().equals("Next Section")) {
            // Get the currently active tabbed pane
            JTabbedPane activeTabbedPane = getActiveTabbedPane();
            // Update the active tabbed pane to go to the next tab
            updateTabbedPane(activeTabbedPane, 1);
        } else if (e.getActionCommand().equals("Previous Section")) {
            // Get the currently active tabbed pane
            JTabbedPane activeTabbedPane = getActiveTabbedPane();
            // Update the active tabbed pane to go to the previous tab
            updateTabbedPane(activeTabbedPane, -1);
        }
    }
        
        private JTabbedPane getActiveTabbedPane() {
            // Get the user and references tabbed panes
            JTabbedPane userTabbedPane = getUserTabbedPane();
            JTabbedPane referencesTabbedPane = getReferencesTabbedPane();
            // Return the currently active tabbed pane
            return mainTabbedPane.getSelectedComponent() == userTabbedPane ? userTabbedPane : referencesTabbedPane;
        }
        
        private void updateTabbedPane(JTabbedPane tabbedPane, int direction) {
            // Get the current index of the selected tab
            int currentIndex = tabbedPane.getSelectedIndex();
            // Calculate the new index based on the direction
            int newIndex = currentIndex + direction;
            // If the new index is valid, set it as the selected index
            if (newIndex >= 0 && newIndex < tabbedPane.getTabCount()) {
                tabbedPane.setSelectedIndex(newIndex);
            }
        }
        
        private JTabbedPane getUserTabbedPane() {
            // Return the user tabbed pane (the first component of the main tabbed pane)
            return (JTabbedPane) mainTabbedPane.getComponent(0);
        }
        
        private JTabbedPane getReferencesTabbedPane() {
            // Return the references tabbed pane (the second component of the main tabbed pane)
            return (JTabbedPane) mainTabbedPane.getComponent(1);
        }

    private void addNavigationButtons() {
        JButton prevSectionButton = new JButton("Previous Section");
        prevSectionButton.addActionListener(this);

        JButton nextSectionButton = new JButton("Next Section");
        nextSectionButton.addActionListener(this);

        JButton addProfileButton = new JButton("Add Profile");
        addProfileButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(prevSectionButton);
        buttonPanel.add(nextSectionButton);
        buttonPanel.add(addProfileButton);

        add(buttonPanel, BorderLayout.SOUTH);

        
    }

    


    // Method to read user profiles from a single file
public List<User> readUserProfilesFromFile(File file) {
    // Initialize an empty list of users
    List<User> users = new ArrayList<>();

    // Try to open the file with a BufferedReader
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        // Initialize variables to hold user data
        String title = "";
        String name = "";
        String email = "";
        String references = "";
        // Flags to track which section of the file we're in
        boolean isUserSection = false;
        boolean isReferenceSection = false;

        // Read the file line by line
        while ((line = reader.readLine()) != null) {
            // Split the line into fields
            String[] fields = line.split(",");
            // Check if the line is a header line and skip it
            if (fields[0].equalsIgnoreCase("Section") && fields[1].equalsIgnoreCase("Sub-Section")) {
                continue;
            }

            // Check which section we're in
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

            // If we're in the User section, read the user data
            if (isUserSection) {
                if (fields[1].equalsIgnoreCase("Name")) {
                    name = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                } else if (fields[1].equalsIgnoreCase("Title")) {
                    title = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                } else if (fields[1].equalsIgnoreCase("Email")) {
                    email = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                    // Create a new User object and add it to the list
                    User user = new User(title, name, email, references.trim());
                    users.add(user);
                    // Reset the user data variables
                    title = "";
                    name = "";
                    email = "";
                    references = "";
                }
            // If we're in the References section, read the reference data
            } else if (isReferenceSection) {
                if (fields[1].equalsIgnoreCase("Referee 1") || fields[1].equalsIgnoreCase("Referee 2")) {
                    String referenceText = String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                    // Replace special characters with newlines
                    referenceText = referenceText.replaceAll("%%%%", "\n");
                    referenceText = referenceText.replaceAll("/////", "\n");
                    references += referenceText + "\n\n";
                    // If we've read both references, create a new User object and add it to the list
                    if (fields[1].equalsIgnoreCase("Referee 2")) {
                        User user = new User(title, name, email, references.trim());
                        users.add(user);
                        // Reset the user data variables
                        title = "";
                        name = "";
                        email = "";
                        references = "";
                    }
                }
            }
        }
    // Catch any IOExceptions that occur
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Return the list of users
    return users;
}

// Method to read user profiles from all files
public List<User> readUserProfilesFromAllFiles() {
    // Initialize an empty list of users
    List<User> users = new ArrayList<>();
    // For each file in the list of files to read
    for (File file : filesToRead) {
        // Read the user profiles from the file and add them to the list
        users.addAll(readUserProfilesFromFile(file));
    }
    // Return the list of users
    return users;
    }
}
