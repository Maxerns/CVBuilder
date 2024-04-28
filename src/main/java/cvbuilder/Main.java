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




