/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder;


import cvbuilder.model.User;
import cvbuilder.view.GUIViewer;
import java.util.List;

/**
 *
 * @author mxgar
 */
public class Main {

    public static void main(String[] args) {

    GUIViewer viewer = GUIViewer.getInstance(); 
    List<User> users = viewer.readUserProfilesFromAllFiles();
    viewer = new GUIViewer(users); 
    viewer.showViewer(); 

    


}

}




