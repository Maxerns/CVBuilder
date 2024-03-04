/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.model;

/**
 *
 * @author mxgar
 */
public class User
{
    
        private String userProfileID;
        private String title;
        private String name;
        private String email;

        public User(String userProfileID, String title, String name, String email) {
            this.userProfileID = userProfileID;
            this.title = title;
            this.name = name;
            this.email = email;
        }

        public String getUserProfileID() {
            return userProfileID;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
        
        
    @Override
    public String toString()
    {
        return "User{" + "name=" + name + '}';
    }
    
    
    public String getFormattedText() {
        return String.format("{\nuserProfileID:%s\ntitle:%s\nname:%s\nemail:%s\n}\n",
                userProfileID, title, name, email);
    }
}


