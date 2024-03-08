/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.view;

import cvbuilder.model.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
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

    private JTabbedPane tabbedPane;
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
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  tabbedPane = new JTabbedPane();

    // Create tabs for different user attributes
    JPanel userTitlePanel = new JPanel();
    userTitlePanel.setLayout(new BoxLayout(userTitlePanel, BoxLayout.Y_AXIS));
    tabbedPane.addTab("User Title", userTitlePanel);

    JPanel userNamePanel = new JPanel();
    userNamePanel.setLayout(new BoxLayout(userNamePanel, BoxLayout.Y_AXIS));
    tabbedPane.addTab("User Name", userNamePanel);

    JPanel userEmailPanel = new JPanel();
    userEmailPanel.setLayout(new BoxLayout(userEmailPanel, BoxLayout.Y_AXIS));
    tabbedPane.addTab("User Email", userEmailPanel);

    JPanel referencePanel = new JPanel();
    referencePanel.setLayout(new BoxLayout(referencePanel, BoxLayout.Y_AXIS));
    tabbedPane.addTab("References", referencePanel);
    
    // Add user profiles to the respective tabs
    int userCount = 1;
    for (User user : users) {
        String[] titles = user.getTitle().split(",");
        String[] names = user.getName().split(",");
        String[] emails = user.getEmail().split(",");

        for (String title : titles) {
            UserPanel userTitlePanel1 = new UserPanel(userCount, "User " + userCount, title.trim());
            userTitlePanel.add(userTitlePanel1);
            userTitlePanel1.setUser(user); // Associate UserPanel with User object
        }

        for (String name : names) {
            UserPanel userNamePanel1 = new UserPanel(userCount, "User " + userCount, name.trim());
            userNamePanel.add(userNamePanel1);
            userNamePanel1.setUser(user); // Associate UserPanel with User object
        }

        for (String email : emails) {
            UserPanel userEmailPanel1 = new UserPanel(userCount, "User " + userCount, email.trim());
            userEmailPanel.add(userEmailPanel1);
            userEmailPanel1.setUser(user); // Associate UserPanel with User object
           
             ReferencePanel referencePanel1 = new ReferencePanel(user.getReferences());
        referencePanel.add(referencePanel1);
        }
        
        userCount++;
    }

    add(tabbedPane);
}

    public void showViewer() {
        setVisible(true);
    }
    
//    for (User user : User) {
//    UserPanel userPanel = new UserPanel(User);
//    userPanel.add(userPanel);
//
//}

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
        String userProfileID = "";
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
                    User user = new User(userProfileID, title, name, email, references);
                    users.add(user);
                    userProfileID = "";
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
                    User user = new User(userProfileID, title, name, email, references);
                    users.add(user);
                    userProfileID = "";
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