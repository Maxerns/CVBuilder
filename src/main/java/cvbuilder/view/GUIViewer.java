package cvbuilder.view;

import cvbuilder.model.User;
import java.awt.BorderLayout;
import java.awt.Component;
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
    private JTabbedPane mainTabbedPane; // The main tabbed pane for the GUI
    private static GUIViewer instance; // The singleton instance of the GUIViewer class
    private List<File> filesToRead; // The list of files to read user profiles from
    private File lastAddedFile; // The last file added to the list of files to read
    private Preferences prefs; // The preferences object for storing the last added file
    private String references; // The references for the user profiles
    

       // This is the constructor for the GUIViewer class.
       private GUIViewer() {
        // Initialize the list of files to read and add a default file.
        filesToRead = new ArrayList<>();
        filesToRead.add(new File("data\\cv_repo_3.csv"));
        // Initialize the preferences.
        initPrefs();
    }

    // This method initializes the preferences.
    private void initPrefs() {
        // If the preferences have not been initialized yet, do so now.
        if (prefs == null) {
            // Get the preferences node for the GUIViewer class.
            prefs = Preferences.userNodeForPackage(GUIViewer.class);
            // Get the name of the last added file from the preferences.
            String lastAddedFileName = prefs.get("lastAddedFile", null);
            // If a last added file was found, add it to the list of files to read.
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
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(quitItem);
        jmb.add(fileMenu);
        setJMenuBar(jmb);
        quitItem.addActionListener(this);
        loadItem.addActionListener(this);
        saveItem.addActionListener(this);

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

    // This method adds references to a tabbed pane in the GUI.
    private void addReferencesToPanel(User user, JTabbedPane referencesTabbedPane) {
        // Get the user's references, trim any leading or trailing whitespace, and split them into an array.
        String[] references = user.getReferences().trim().split(",");
        // Loop through each reference.
        for (int i = 0; i < references.length; i++) {
            // Get the current reference.
            String reference = references[i];
            // Create a new ReferencePanel with the reference.
            ReferencePanel refPanel = new ReferencePanel(reference);
            // If this is the first reference, add it as "Referee 1". If it's the second, add it as "Referee 2", and so on.
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


      // This method is an event handler for various actions performed in the GUI.
      @Override
      public void actionPerformed(ActionEvent e) {
          // Initialize preferences.
          initPrefs();
  
          // If the "Quit" button is clicked, terminate the program.
          if (e.getActionCommand().equals("Quit")) {
              System.exit(0);
          } 
          // If the "Load" button is clicked, open a file chooser for the user to select a file.
          else if (e.getActionCommand().equals("Load")) {
              JFileChooser fileChooser = new JFileChooser();
              fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
              int result = fileChooser.showOpenDialog(this);
  
              // If a file is selected, copy it to the "data" directory and add it to the list of files to read.
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
          } 
          // If the "Save" button is clicked, save the user data.
          else if (e.getActionCommand().equals("Save")) {
              saveUserData();
          } 
          // If the "Add Profile" button is clicked, open a dialog for the user to enter a new user profile.
          else if (e.getActionCommand().equals("Add Profile")) {
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
  
              // If the user clicks "OK", validate the input and add the new user profile.
              if (result == JOptionPane.OK_OPTION) {
                  String title = titleField.getText();
                  String name = nameField.getText();
                  String email = emailField.getText();
  
                  // Validate that all fields are filled out.
                  if (title.isEmpty() || name.isEmpty() || email.isEmpty()) {
                      JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                      return;
                  }
  
                  // Validate that the email address is in a valid format.
                  if (!email.contains("@") || email.indexOf('@') > email.lastIndexOf('.')) {
                      JOptionPane.showMessageDialog(this, "Invalid email address. Please include '@' and a domain in the email.", "Error", JOptionPane.ERROR_MESSAGE);
                      return;
                  }
  
                  // Create a new User object and append it to the CSV file.
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

    // This method is responsible for adding navigation buttons to the GUI.
    private void addNavigationButtons() {
        // Create a "Previous Section" button and add this class as its action listener.
        JButton prevSectionButton = new JButton("Previous Section");
        prevSectionButton.addActionListener(this);

        // Create a "Next Section" button and add this class as its action listener.
        JButton nextSectionButton = new JButton("Next Section");
        nextSectionButton.addActionListener(this);

        // Create an "Add Profile" button and add this class as its action listener.
        JButton addProfileButton = new JButton("Add Profile");
        addProfileButton.addActionListener(this);

        // Create a panel with a centered flow layout to hold the buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Add the buttons to the panel.
        buttonPanel.add(prevSectionButton);
        buttonPanel.add(nextSectionButton);
        buttonPanel.add(addProfileButton);

        // Add the panel to the south region of this class (which should be a container like a JFrame or JPanel).
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // This method is responsible for saving user data to a CSV file.
    private void saveUserData() {
        // Fetch the selected user data from the user panels.
        List<String> selectedNames = getSelectedUserData("Name");
        List<String> selectedTitles = getSelectedUserData("Title");
        List<String> selectedEmails = getSelectedUserData("Email");
        List<String> selectedReferences = getSelectedReferences();
    
        // Combine the selected data into a single CSV line.
        StringBuilder csvLine = new StringBuilder();
        for (int i = 0; i < selectedNames.size(); i++) {
                csvLine.append(selectedNames.get(i)).append(",");
                csvLine.append(selectedTitles.get(i)).append(",");
                csvLine.append(selectedEmails.get(i)).append(",");
                csvLine.append(selectedReferences.get(i)).append("\n");
            }
        
        // Create a file chooser for the user to select where to save the CSV data.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save User Data");
        fileChooser.setSelectedFile(new File("cv_data.csv"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Write the CSV data to the selected file.
                FileWriter fileWriter = new FileWriter(selectedFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(csvLine.toString());
                bufferedWriter.close();
                fileWriter.close();
                // Show a message dialog to confirm the data has been saved.
                JOptionPane.showMessageDialog(this, "User data saved to: " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                // If an error occurs, print the stack trace and show an error message.
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving user data: " + ex.getMessage());
            }
        }
    }
    
    // This method fetches the selected user data of a specific type from the user panels.
    private List<String> getSelectedUserData(String dataType) {
        List<String> selectedData = new ArrayList<>();
        JTabbedPane userTabbedPane = getUserTabbedPane();
        for (int i = 0; i < userTabbedPane.getTabCount(); i++) {
            JPanel panel = (JPanel) userTabbedPane.getComponentAt(i);
            for (Component component : panel.getComponents()) {
                // If the component is a UserPanel and its data type matches the requested type,
                // check if the include checkbox and data radio button are selected.
                if (component instanceof UserPanel && ((UserPanel) component).getDataType().equals(dataType)) {
                    if (((UserPanel) component).getIncludeCheckBox().isSelected()) {
                        if (((UserPanel) component).getDataRadioButton().isSelected()) {
                            // If both are selected, add the data to the selected data list.
                            selectedData.add(((UserPanel) component).getData());
                        }
                    } else {
                        // If the include checkbox is not selected, add an empty string to the list.
                        selectedData.add("");
                    }
                }
            }
        }
        return selectedData;
    }

    // This method fetches the selected references from the references panels.
    private List<String> getSelectedReferences() {
        List<String> selectedReferences = new ArrayList<>();
        JTabbedPane referencesTabbedPane = getReferencesTabbedPane();
        for (int i = 0; i < referencesTabbedPane.getTabCount(); i++) {
            ReferencePanel panel = (ReferencePanel) referencesTabbedPane.getComponentAt(i);
            // If the radio button on the panel is selected, add the reference text to the list.
            if (panel.getRadioButton().isSelected()) {
                selectedReferences.add(panel.getReferenceText());
            }
        }
        return selectedReferences;
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
                    referenceText = referenceText.replaceAll("////", "\n");
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